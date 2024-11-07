package com.voting.survey_host.service;

import com.voting.survey_host.controller.LiveVoteController;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriberImpl implements RedisSubscriber {

    private final LiveVoteController liveVoteController;

    public RedisSubscriberImpl(LiveVoteController liveVoteController) {
        this.liveVoteController = liveVoteController;
    }

    @Override
    public void onMessage(String message) {
        liveVoteController.sendVoteUpdate(message);
    }
}
