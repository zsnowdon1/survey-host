package com.voting.survey_host.service.impl;

import com.voting.survey_host.controller.LiveVoteController;
import com.voting.survey_host.service.RedisSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriberImpl implements RedisSubscriber {

    private final LiveVoteController liveVoteController;

    private static final Logger logger = LoggerFactory.getLogger(RedisSubscriberImpl.class);


    public RedisSubscriberImpl(LiveVoteController liveVoteController) {
        this.liveVoteController = liveVoteController;
    }

    @Override
    public void onMessage(String message) {
        logger.info("Sending kafka message: " + message);
        liveVoteController.sendVoteUpdate(message);
    }
}
