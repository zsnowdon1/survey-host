package com.voting.survey_host.controller;

import com.voting.survey_host.entity.GetSurveyResultsResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/host/surveys")
@CrossOrigin
public interface LiveVoteController {

    @GetMapping("/{surveyId}/results")
    ResponseEntity<GetSurveyResultsResponse> getSurveyResults(@PathVariable("surveyId") String surveyId);

    @GetMapping(value = "/live/{surveyId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter getLiveResults(@PathVariable("surveyId") String surveyId);

    void sendVoteUpdate(String voteData);

}
