package com.voting.survey_host.controller;

import com.voting.entities.SurveyDTO;
import com.voting.entities.SurveyDetailDTO;
import com.voting.survey_host.entity.DeleteSurveyResponse;
import com.voting.survey_host.entity.ToggleStatusResponse;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            surveyDTO.setStatus("NOT-LIVE");
            SurveyDTO newSurvey = surveyService.createSurvey(surveyDTO);
            return new ResponseEntity<>(newSurvey, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{surveyId}")
    public ResponseEntity<SurveyDTO> setSurvey(@PathVariable String surveyId, @RequestBody SurveyDTO surveyDTO) {
        try {
            SurveyDTO output = surveyService.setSurvey(surveyDTO);
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyDTO> getSurvey(@PathVariable String surveyId) {
        try {
            SurveyDTO survey = surveyService.getSurvey(surveyId);
            return new ResponseEntity<>(survey, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<List<SurveyDetailDTO>> getSurveyDetailsByHostname(@RequestParam("hostUsername") String hostUsername) {
        try {
            logger.info("Received getSurveyDetails request");
            List<SurveyDetailDTO> surveys = surveyService.getSurveyDetailsByHostUsername(hostUsername);
            return new ResponseEntity<>(surveys, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{surveyId}/status")
    public ResponseEntity<ToggleStatusResponse> toggleSurveyStatus(@PathVariable String surveyId, @RequestParam String status) {
        try {
            String newStatus = surveyService.toggleSurveyStatus(surveyId, status);
            return new ResponseEntity<>(new ToggleStatusResponse(newStatus), HttpStatus.OK);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{surveyId}")
    public ResponseEntity<DeleteSurveyResponse> deleteSurvey(@PathVariable String surveyId) {
        try {
            surveyService.deleteSurvey(surveyId);
            return new ResponseEntity<>(new DeleteSurveyResponse(surveyId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
