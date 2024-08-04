package com.voting.survey_host.controller;

import com.voting.survey_host.dto.CreateSurveyRequest;
import com.voting.survey_host.dto.CreateSurveyResponse;
import com.voting.survey_host.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/survey")
public class SurveyHostController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping("/createSurvey")
    public ResponseEntity<CreateSurveyResponse> createGame(@RequestBody CreateSurveyRequest request) {

        surveyService.createSurvey(request);
        return null;
    }
}
