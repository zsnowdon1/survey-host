package com.voting.survey_host.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface LiveVoteService {
    SseEmitter streamResults(String surveyId);
}
