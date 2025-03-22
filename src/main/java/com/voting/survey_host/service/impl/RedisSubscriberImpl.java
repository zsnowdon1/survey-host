package com.voting.survey_host.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.survey_host.controller.LiveVoteController;
import com.voting.survey_host.entity.VoteUpdate;
import com.voting.survey_host.service.RedisSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriberImpl implements RedisSubscriber {

    private final LiveVoteController liveVoteController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(RedisSubscriberImpl.class);


    public RedisSubscriberImpl(LiveVoteController liveVoteController) {
        this.liveVoteController = liveVoteController;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = new String(message.getBody());
            logger.info("Received Redis message: " + messageBody);
            // Parse Redis message (e.g., {"choiceId":"...","questionId":"...","votes":8})
            VoteUpdate voteUpdate = objectMapper.readValue(messageBody, VoteUpdate.class);
            liveVoteController.sendVoteUpdate(voteUpdate.getQuestionId(), voteUpdate.getChoiceId(), voteUpdate.getVotes());
        } catch (Exception e) {
            logger.error("Error processing Redis message: " + e.getMessage());
        }
    }
}
