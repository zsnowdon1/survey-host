package com.voting.survey_host.controller;

import com.voting.survey_host.dto.CreateSurveyRequest;
import com.voting.survey_host.dto.CreateSurveyResponse;
import com.voting.survey_host.dto.StartSurveyReponse;
import com.voting.survey_host.dto.StartSurveyRequest;
import com.voting.survey_host.entity.Survey;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveys")
@CrossOrigin(origins = "http:/localhost:3000")
public class SurveyHostController {

    @Autowired
    private SurveyService surveyService;

    private static final Logger logger = LoggerFactory.getLogger(SurveyHostController.class);

    @PostMapping("/createSurvey")
    public ResponseEntity<CreateSurveyResponse> createGame(@RequestBody CreateSurveyRequest request) throws Exception {
        try {
            surveyService.createSurvey(request);
        } catch (Exception e) {
            throw new Exception("Internal service error");
        }
        surveyService.createSurvey(request);
        return null;
    }

    @GetMapping("{hostname}")
    public ResponseEntity<List<Survey>> getSurveysByHost(@PathVariable String hostname) {
        logger.info("Received survey list request for {}", hostname);
        try {
            List<Survey> surveys = surveyService.getSurveysByHost(hostname);
            logger.info("Successfully retrieved surveys for {}", hostname);
            return new ResponseEntity<>(surveys, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.info("Failed retrieving surveys for {}", hostname);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/startSurvey")
//    public ResponseEntity<StartSurveyReponse> startSurvey(@RequestBody StartSurveyRequest request) {
//        try {
//
//        } catch (Exception e) {
//
//        }
//        return null;
//    }

    @GetMapping("/test")
    public ResponseEntity<String> testRequest() {
        logger.info("Received test request");
        return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
    }
}
