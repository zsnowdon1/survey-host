package com.voting.survey_host.controller;

import com.voting.survey_host.dto.AddChoiceRequest;
import com.voting.survey_host.dto.AddQuestionRequest;
import com.voting.survey_host.dto.CreateSurveyRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/surveys")
@CrossOrigin(origins = "http:/localhost:3000")
public class SurveyHostController {

    private final SurveyService surveyService;

    private static final Logger logger = LoggerFactory.getLogger(SurveyHostController.class);

    public SurveyHostController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping("/survey")
    public ResponseEntity<String> setSurvey(@RequestBody Survey survey) {
        try {

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @PostMapping("/createSurvey")
    public ResponseEntity<Long> createEmptySurvey(@RequestBody CreateSurveyRequest survey) {
        logger.info("Received request to create new survey {}", survey.getTitle());
        try {
            long surveyId = surveyService.createEmptySurvey(survey.getTitle());
            return new ResponseEntity<>(surveyId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Will change to JWT when ready
    @GetMapping("/hostname/{hostname}")
    public ResponseEntity<List<Survey>> getSurveyList(@PathVariable String hostname) {
        logger.info("Received survey list request for {}", hostname);
        try {
            List<Survey> surveys = surveyService.getSurveysByHost(hostname);
            logger.info("Successfully retrieved surveys for {}", hostname);
            return new ResponseEntity<>(surveys, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Failed retrieving surveys for {}", hostname);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<Survey> getSurveyDetails(@PathVariable("surveyId") long surveyId) {
        logger.info("Received get survey request for surveyId {}", surveyId);
        try {
            Survey survey = surveyService.getSurveyById(surveyId);
            return new ResponseEntity<>(survey, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Question> getQuestionDetails(@PathVariable("questionId") long questionId) {
        logger.info("Received get question request for surveyId {}", questionId);
        try {
            Question question = surveyService.getQuestionById(questionId);
            List<Choice> choices = surveyService.getChoiceList(questionId);
            question.setChoices(choices);
            return new ResponseEntity<>(question, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
