package com.voting.survey_host.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.survey_host.entity.GetSurveyResultsResponse;
import com.voting.entities.VoteUpdate;
import com.voting.survey_host.service.LiveVoteService;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final RedisTemplate<String, Object> messageRedisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final SurveyService surveyResultService;
    private static final String REDIS_SURVEY_RESULT_PREFIX = "survey:hosts:";
    private static final Logger logger = LoggerFactory.getLogger(LiveVoteServiceImpl.class);

    public LiveVoteServiceImpl(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate,
                               @Qualifier("messageRedisTemplate") RedisTemplate<String, Object> messageRedisTemplate,
                               RedisMessageListenerContainer redisMessageListenerContainer,
                               SurveyService surveyResultService) {
        this.redisTemplate = redisTemplate;
        this.messageRedisTemplate = messageRedisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.surveyResultService = surveyResultService;
    }

    @Override
    public SseEmitter streamResults(String surveyId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String key = REDIS_SURVEY_RESULT_PREFIX + surveyId;
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
        logger.debug("Checking subscription for survey {}, emitter count: {}", surveyId, surveyEmitters != null ? surveyEmitters.size() : 0);
        if (surveyEmitters != null && surveyEmitters.size() == 1) {
            MessageListener listener = (message, pattern) -> {
                try {
                    VoteUpdate voteUpdate = (VoteUpdate) messageRedisTemplate.getValueSerializer().deserialize(message.getBody());
                    logger.info("Received Redis message on channel {}: {}", channel, voteUpdate);
                    sendVoteUpdate(surveyId, voteUpdate);
                } catch (Exception e) {
                    logger.error("Error processing Redis message for survey {}: {}", surveyId, e.getMessage());
                }
            };
            listeners.put(surveyId, listener);
            redisMessageListenerContainer.addMessageListener(listener, new ChannelTopic(channel));
            logger.info("Subscribed to Redis channel: {}", channel);
        } else {
            logger.debug("Subscription skipped for survey {}, already subscribed or no emitters", surveyId);
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

    public void sendVoteUpdate(String surveyId, VoteUpdate voteUpdate) {
        try {
            String voteData = new ObjectMapper().writeValueAsString(voteUpdate);
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
