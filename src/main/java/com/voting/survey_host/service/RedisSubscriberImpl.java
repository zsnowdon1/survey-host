package com.voting.survey_host.service;

import com.voting.survey_host.controller.LiveVoteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriberImpl implements RedisSubscriber {

    @Autowired
    private LiveVoteController liveVoteController;

    @Override
    public void onMessage(String message) {
        liveVoteController.sendVoteUpdate(message);
    }
}
