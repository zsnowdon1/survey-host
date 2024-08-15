package com.voting.survey_host.controller;

import com.voting.survey_host.dto.CreateSurveyRequest;
import com.voting.survey_host.dto.CreateSurveyResponse;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import com.voting.survey_host.service.SurveyService;
import org.apache.coyote.Response;
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

    @GetMapping("/hostname/{hostname}")
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

    @GetMapping("/{surveyId}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable("surveyId") int surveyId) {
        logger.info("Received get survey request for surveyId {}", surveyId);
        try {
            Survey survey = surveyService.getSurveyById((long) surveyId);
            return new ResponseEntity<>(survey, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/questions/{surveyId}")
    public ResponseEntity<List<Question>> getQuestionsBySurvey(@PathVariable("surveyId") String surveyId) {
        logger.info("Received get questions request for surveyId {}", surveyId);
        try {
            List<Question> questions = surveyService.getQuestionsBySurvey(Long.parseLong(surveyId));
            return new ResponseEntity<>(questions, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/choices/{questionId}")
    public ResponseEntity<List<Choice>> getChoicesByQuestions(@PathVariable("questionId") String questionId) {
        logger.info("Received get choices request for questionId {}", questionId);
        try {
            List<Choice> choices = surveyService.getChoicesByQuestion(Long.parseLong(questionId));
            return new ResponseEntity<>(choices, HttpStatus.ACCEPTED);
        } catch (Exception e) {
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

//    @GetMapping("/test")
//    public ResponseEntity<String> testRequest() {
//        logger.info("Received test request");
//        return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
//    }
}
