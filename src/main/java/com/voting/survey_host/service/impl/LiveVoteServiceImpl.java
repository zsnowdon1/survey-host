package com.voting.survey_host.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.survey_host.entity.GetSurveyResultsResponse;
import com.voting.entities.VoteUpdate;
import com.voting.survey_host.service.LiveVoteService;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class LiveVoteServiceImpl implements LiveVoteService {

    private final Map<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final Map<String, MessageListener> listeners = new ConcurrentHashMap<>();
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final SurveyService surveyResultService;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(LiveVoteServiceImpl.class);

    public LiveVoteServiceImpl(RedisTemplate<String, Object> redisTemplate, RedisMessageListenerContainer redisMessageListenerContainer,
                               SurveyService surveyResultService, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.surveyResultService = surveyResultService;
        this.objectMapper = objectMapper;
    }

    @Override
    public SseEmitter streamResults(String surveyId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String key = "survey:" + surveyId + ":active-hosts";
        String sessionId = UUID.randomUUID().toString();

        // Register host presence with TTL
        redisTemplate.opsForSet().add(key, sessionId);
        redisTemplate.expire(key, 300, java.util.concurrent.TimeUnit.SECONDS); // 5 min TTL

        // Add emitter and subscribe
        CopyOnWriteArrayList<SseEmitter> surveyEmitters = emitters.computeIfAbsent(surveyId, k -> new CopyOnWriteArrayList<>());
        surveyEmitters.add(emitter);
        subscribeToSurveyResults(surveyId);

        // Cleanup
        emitter.onCompletion(() -> cleanup(surveyId, emitter, key, sessionId));
        emitter.onError((e) -> cleanup(surveyId, emitter, key, sessionId));
        emitter.onTimeout(() -> cleanup(surveyId, emitter, key, sessionId));

        // Send initial data
        sendInitialData(emitter, surveyId);
        return emitter;
    }

    private void subscribeToSurveyResults(String surveyId) {
        String channel = "survey:" + surveyId + ":results";
        CopyOnWriteArrayList<SseEmitter> surveyEmitters = emitters.get(surveyId);
        if (surveyEmitters != null && surveyEmitters.size() == 1) {
            MessageListener listener = (message, pattern) -> {
                try {
                    String messageBody = new String(message.getBody());
                    logger.info("Received Redis message on channel {}: {}", channel, messageBody);
                    VoteUpdate voteUpdate = objectMapper.readValue(messageBody, VoteUpdate.class);
                    sendVoteUpdate(surveyId, voteUpdate.getQuestionId(), voteUpdate.getChoiceId(), voteUpdate.getVotes());
                } catch (IOException e) {
                    logger.error("Error processing Redis message for survey {}: {}", surveyId, e.getMessage());
                }
            };
            listeners.put(surveyId, listener); // Store the listener
            redisMessageListenerContainer.addMessageListener(listener, new ChannelTopic(channel));
            logger.info("Subscribed to Redis channel: {}", channel);
        }
    }

    private void sendInitialData(SseEmitter emitter, String surveyId) {
        try {
            Map<String, Map<String, Long>> result = surveyResultService.getInitialResults(surveyId);
            GetSurveyResultsResponse output = new GetSurveyResultsResponse(result);
            emitter.send(SseEmitter.event().name("initial-data").data(output));
        } catch (Exception e) {
            emitter.completeWithError(e);
            logger.info("Error sending initial results for surveyId: " + surveyId);
        }
    }

    public void sendVoteUpdate(String surveyId, String questionId, String choiceId, long votes) {
        VoteUpdate update = new VoteUpdate(questionId, choiceId, votes);
        try {
            String voteData = objectMapper.writeValueAsString(update);
            logger.info("Sending vote update for survey {}: {}", surveyId, voteData);
            CopyOnWriteArrayList<SseEmitter> surveyEmitters = emitters.get(surveyId);
            if (surveyEmitters != null) {
                surveyEmitters.forEach(emitter -> {
                    try {
                        emitter.send(SseEmitter.event().name("vote-update").data(voteData));
                    } catch (IOException e) {
                        logger.error("Failed to send vote update to emitter for survey {}: {}", surveyId, e.getMessage());
                        surveyEmitters.remove(emitter);
                    }
                });
            }
        } catch (IOException e) {
            logger.error("Error serializing vote update for survey {}: {}", surveyId, e.getMessage());
        }
    }

    private void cleanup(String surveyId, SseEmitter emitter, String activeHostsKey, String sessionId) {
        redisTemplate.opsForSet().remove(activeHostsKey, sessionId);
        logger.debug("Removed session {} from active hosts for survey {}", sessionId, surveyId);

        CopyOnWriteArrayList<SseEmitter> surveyEmitters = emitters.get(surveyId);
        if (surveyEmitters != null) {
            surveyEmitters.remove(emitter);
            if (surveyEmitters.isEmpty()) {
                emitters.remove(surveyId);
                MessageListener listener = listeners.remove(surveyId);
                if (listener != null) {
                    redisMessageListenerContainer.removeMessageListener(listener, new ChannelTopic("survey:" + surveyId + ":results"));
                    logger.info("Unsubscribed from Redis channel: survey:{}:results", surveyId);
                }
            }
        }
    }
}
