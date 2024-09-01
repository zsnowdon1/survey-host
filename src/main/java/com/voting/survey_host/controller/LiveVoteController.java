package com.voting.survey_host.controller;

import com.voting.survey_host.service.SurveyResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/surveys")
@CrossOrigin(origins = "http:/localhost:3000")
public class LiveVoteController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Autowired
    private SurveyResultService surveyResultService;

    private static final Logger logger = LoggerFactory.getLogger(LiveVoteController.class);

    @GetMapping("/{surveyId}/results")
    public ResponseEntity<Map<String, Map<String, Long>>> getSurveyResults(@PathVariable("surveyId") String surveyId) {
        logger.info("Fetching survey results for survey " + surveyId);
        Map<String, Map<String, Long>> results = surveyResultService.getInitialResults(surveyId);
        logger.info("Received results " + results.size());
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(value = "/{surveyId}/liveResults", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
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
            Map<String, Map<String, Long>> results = surveyResultService.getInitialResults(surveyId);
            emitter.send(SseEmitter.event().name("initial-data").data(results));
        } catch (Exception e) {
            emitter.completeWithError(e);
            logger.info("Error sending initial results for surveyId: " + surveyId);
        }
    }

}
