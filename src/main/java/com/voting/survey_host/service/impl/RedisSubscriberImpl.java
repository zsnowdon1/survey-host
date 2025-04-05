package com.voting.survey_host.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.survey_host.controller.LiveVoteController;
import com.voting.entities.VoteUpdate;
import com.voting.survey_host.service.RedisSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriberImpl implements RedisSubscriber {

    private final LiveVoteServiceImpl liveVoteService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(RedisSubscriberImpl.class);

    public RedisSubscriberImpl(LiveVoteServiceImpl liveVoteService) {
        this.liveVoteService = liveVoteService;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = new String(message.getBody());
            String channel = new String(message.getChannel());
            String surveyId = channel.split(":")[1];
            logger.info("Received Redis message on channel {}: {}", channel, messageBody);
            VoteUpdate voteUpdate = objectMapper.readValue(messageBody, VoteUpdate.class);
            liveVoteService.sendVoteUpdate(surveyId, voteUpdate.getQuestionId(), voteUpdate.getChoiceId(), voteUpdate.getVotes());
        } catch (Exception e) {
            logger.error("Error processing Redis message: " + e.getMessage());
        }
    }
}
