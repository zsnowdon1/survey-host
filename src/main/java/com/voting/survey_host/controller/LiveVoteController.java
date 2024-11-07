package com.voting.survey_host.controller;

import com.voting.survey_host.entity.ChoiceMapping;
import com.voting.survey_host.entity.QuestionVotes;
import com.voting.survey_host.service.SurveyResultService;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/surveys")
@CrossOrigin(origins = "http:/localhost:3000")
public class LiveVoteController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private final SurveyResultService surveyResultService;

    private final SurveyService surveyService;

    private static final Logger logger = LoggerFactory.getLogger(LiveVoteController.class);

    public LiveVoteController(SurveyResultService surveyResultService, SurveyService surveyService) {
        this.surveyResultService = surveyResultService;
        this.surveyService = surveyService;
    }

    @GetMapping("/{surveyId}/results")
    public ResponseEntity<List<QuestionVotes>> getSurveyResults(@PathVariable("surveyId") String surveyId) {
        logger.info("Fetching survey results for survey " + surveyId);
        List<QuestionVotes> results = surveyResultService.getInitialResults(surveyId);
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

    @GetMapping("/{surveyId}/choices")
    public ResponseEntity<List<ChoiceMapping>> getChoiceMappings(@PathVariable("surveyId") long surveyId) {
        logger.info("Received get choice mappings for survey " + surveyId);
        try {
            List<ChoiceMapping> choiceMap = surveyService.getChoicesBySurvey(surveyId);
            return new ResponseEntity<>(choiceMap, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendInitialData(SseEmitter emitter, String surveyId) {
        try {
            List<QuestionVotes> results = surveyResultService.getInitialResults(surveyId);
            emitter.send(SseEmitter.event().name("initial-data").data(results));
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
