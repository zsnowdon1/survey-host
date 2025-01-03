package com.voting.survey_host.controller;

import com.voting.survey_host.entity.SurveyDTO;
import com.voting.survey_host.mongoData.Survey;
import com.voting.survey_host.service.SurveyService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/surveys")
@CrossOrigin
public class SurveyHostController {

    private final SurveyService surveyService;

    private static final Logger logger = LoggerFactory.getLogger(SurveyHostController.class);

    public SurveyHostController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping()
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO surveyDTO) {
        try {
            SurveyDTO newSurvey = surveyService.createEmptySurvey(surveyDTO);
            return new ResponseEntity<>(newSurvey, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{surveyId}")
    public ResponseEntity<SurveyDTO> setSurvey(@PathVariable String surveyId, @RequestBody SurveyDTO surveyDTO) {
        try {
            SurveyDTO output = surveyService.setSurvey(surveyDTO);
            return new ResponseEntity<>(output, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
