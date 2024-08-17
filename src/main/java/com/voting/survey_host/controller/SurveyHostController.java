package com.voting.survey_host.controller;

import com.voting.survey_host.dto.AddChoiceRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveys")
@CrossOrigin(origins = "http:/localhost:3000")
public class SurveyHostController {

    @Autowired
    private SurveyService surveyService;

    private static final Logger logger = LoggerFactory.getLogger(SurveyHostController.class);

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
            List<Question> questions = surveyService.getQuestionList(surveyId);
            survey.setQuestionList(questions);
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
        try {
            Choice choice = surveyService.addChoice(addChoiceRequest.getQuestionId(), addChoiceRequest.getNewChoice());
            if(choice.getChoiceId() != -1) {
                return new ResponseEntity<>(choice.getChoiceId(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/choices/{choiceId}")
    public ResponseEntity<Long> deleteChoice(@PathVariable("choiceId") long choiceId) {
        logger.info("Received request to delete choice {}", choiceId);
        try {
            long rowsAffected = surveyService.deleteChoice(choiceId);
            if(rowsAffected > 0) {
                return new ResponseEntity<>(choiceId, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/{surveyId}/questions")
//    public ResponseEntity<List<Question>> getQuestionsBySurvey(@PathVariable("surveyId") long surveyId) {
//        logger.info("Received get questions request for surveyId {}", surveyId);
//        try {
//            List<Question> questions = surveyService.getQuestionsBySurvey(surveyId);
//            return new ResponseEntity<>(questions, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }


//    @GetMapping("/choices/{questionId}")
//    public ResponseEntity<List<Choice>> getChoicesByQuestions(@PathVariable("questionId") long questionId) {
//        logger.info("Received get choices request for questionId {}", questionId);
//        try {
//            List<Choice> choices = surveyService.getChoicesByQuestion(questionId);
//            return new ResponseEntity<>(choices, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
