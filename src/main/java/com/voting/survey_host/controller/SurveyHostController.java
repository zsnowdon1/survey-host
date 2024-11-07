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

    @PutMapping("/addChoice")
    public ResponseEntity<Long> addChoice(@RequestBody AddChoiceRequest addChoiceRequest) {
        logger.info("Received add choice request for question {}", addChoiceRequest.getQuestionId());
        try {
            long choiceId = surveyService.addChoice(addChoiceRequest.getQuestionId(), addChoiceRequest.getNewChoice());
            if(choiceId != -1) {
                return new ResponseEntity<>(choiceId, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/addQuestion")
    public ResponseEntity<Long> addQuestion(@RequestBody AddQuestionRequest newQuestion) {
        logger.info("Received add question request for survey {}", newQuestion.getSurveyId());
        try {
            long questionId = surveyService.addQuestion(newQuestion);
            if(questionId != -1) {
                return new ResponseEntity<>(questionId, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/survey/{surveyId}")
    public ResponseEntity<Long> deleteSurvey(@PathVariable("surveyId") long surveyId) {
        logger.info("Received request to delete survey {}", surveyId);
        try {
            int rowsAffected = surveyService.deleteSurvey(surveyId);
            if(rowsAffected > 0) {
                return new ResponseEntity<>(surveyId, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/choices/{choiceId}")
    public ResponseEntity<Long> deleteChoice(@PathVariable("choiceId") long choiceId) {
        logger.info("Received request to delete choice {}", choiceId);
        try {
            int rowsAffected = surveyService.deleteChoice(choiceId);
            if(rowsAffected > 0) {
                return new ResponseEntity<>(choiceId, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Long> deleteQuestion(@PathVariable("questionId") long questionId) {
        logger.info("Received request to delete question {}", questionId);
        try {
            long rowsAffected = surveyService.deleteQuestion(questionId);
            if(rowsAffected > 0) {
                return new ResponseEntity<>(questionId, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
