package com.voting.survey_host.controller;

import com.voting.survey_host.entity.GetSurveyResultsResponse;
import com.voting.survey_host.service.impl.LiveVoteServiceImpl;
import com.voting.survey_host.service.impl.SurveyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/host/surveys")
@CrossOrigin
public class LiveVoteController {

    private final Map<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final SurveyServiceImpl surveyService;
    private final LiveVoteServiceImpl liveVoteService;
    private static final Logger logger = LoggerFactory.getLogger(LiveVoteController.class);

    public LiveVoteController(SurveyServiceImpl surveyService, LiveVoteServiceImpl liveVoteService) {
        this.surveyService = surveyService;
        this.liveVoteService = liveVoteService;
    }

    /**
     * Retrieves JSON format survey results from redis
     * @param surveyId String: id of survey
     * @return key1 = question ID, key2 = choice ID, value1 = vote count
     */
    @GetMapping("/{surveyId}/results")
    public ResponseEntity<GetSurveyResultsResponse> getSurveyResults(@PathVariable("surveyId") String surveyId) {
        logger.info("Fetching survey results for survey " + surveyId);
        Map<String, Map<String, Long>> results = surveyService.getInitialResults(surveyId);
        GetSurveyResultsResponse output = new GetSurveyResultsResponse(results);
        logger.info("Received results " + results.size());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    /**
     * Sse emitter to share live results with a host for a specific survey. Initial results are sent, then
     * votes are sent 1 at a time as they are received from redis.
     * @param surveyId Survey the host is streaming results for
     * @return SseEmitter which shares the live results to host
     */
    @GetMapping(value = "/live/{surveyId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamResults(@PathVariable("surveyId") String surveyId) {
        logger.info("Streaming live results for survey {}", surveyId);
        return liveVoteService.streamResults(surveyId);
    }

}
