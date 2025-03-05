package com.voting.survey_host.controller.impl;

import com.voting.survey_host.controller.LiveVoteController;
import com.voting.survey_host.entity.GetSurveyResultsResponse;
import com.voting.survey_host.service.SurveyResultService;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class LiveVoteControllerImpl implements LiveVoteController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private final SurveyResultService surveyResultService;

    private final SurveyService surveyService;

    private static final Logger logger = LoggerFactory.getLogger(LiveVoteControllerImpl.class);

    public LiveVoteControllerImpl(SurveyResultService surveyResultService, SurveyService surveyService) {
        this.surveyResultService = surveyResultService;
        this.surveyService = surveyService;
    }

    /**
     * Retrieves JSON format survey results from redis
     * @param surveyId String: id of survey
     * @return key1 = question ID, key2 = choice ID, value1 = vote count
     */
    public ResponseEntity<GetSurveyResultsResponse> getSurveyResults(@PathVariable("surveyId") String surveyId) {
        logger.info("Fetching survey results for survey " + surveyId);
        Map<String, Map<String, Long>> results = surveyResultService.getInitialResults(surveyId);
        GetSurveyResultsResponse output = new GetSurveyResultsResponse(results);
        logger.info("Received results " + results.size());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    public SseEmitter getLiveResults(@PathVariable("surveyId") String surveyId) {
        SseEmitter emitter = new SseEmitter();
        emitters.put(surveyId, emitter);

        emitter.onCompletion(() -> emitters.remove(surveyId));
        emitter.onError((e) -> emitters.remove(surveyId));
        emitter.onTimeout(() -> emitters.remove(surveyId));

        sendInitialData(emitter, surveyId);

        return emitter;
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

    public void sendVoteUpdate(String voteData) {
        logger.info("Sending voteData " + voteData);
        emitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("vote-update").data(voteData));
            } catch (Exception e) {
                logger.error("Failed emitting voteData " + voteData);
                emitters.remove(clientId);
            }
        });
    }

}
