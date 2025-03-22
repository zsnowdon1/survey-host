package com.voting.survey_host.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Service
public interface RedisSubscriber {

    void onMessage(Message message, byte[] pattern);
}
