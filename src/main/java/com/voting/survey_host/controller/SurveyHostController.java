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
@RequestMapping("/api/host/surveys")
@CrossOrigin
public class SurveyHostController {

    private final SurveyService surveyService;

    private static final Logger logger = LoggerFactory.getLogger(SurveyHostController.class);

    public SurveyHostController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /**
     * Allows host to post survey details to create a new survey
     * @param surveyDTO Survey details that the host just created
     * @return SurveyDTO The new surveydto which was added to mongodb
     */
    @PostMapping()
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO surveyDTO) {
        try {
            SurveyDTO newSurvey = surveyService.createSurvey(surveyDTO);
            return new ResponseEntity<>(newSurvey, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint that allows a host to post a survey which updates the existing survey in mongodb
     * @param surveyId SurveyId of the survey being updated
     * @param surveyDTO Updated survey DTO
     * @return surveyDTO The survey details which have been updated to mongodb
     */
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

    /**
     * Returns all information for a survey by surveyId
     * @param surveyId  ID of the survey which user is requesting details from
     * @return SurveyDTO DTO which contains all details for a specific survey
     */
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

    /**
     * Returns list of survey details for a specific user.
     * @param hostUsername UserId of the admin/host
     * @return List<SurveyDetailDTO> list of all surveys for a specific host user.
     */
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

    /**
     * Allows host to make a survey LIVE/NOT-LIVE which allows users to access survey
     * @param surveyId SurveyID of the Survey the host wants to make live or turn off
     * @param status Which status the user wants to set the survey to: LIVE, NOT-LIVE
     * @return ToggleStatusResponse Contains the new status and accessCode if the survey is now live
     */
    @PutMapping("/{surveyId}/status")
    public ResponseEntity<ToggleStatusResponse> toggleSurveyStatus(@PathVariable String surveyId, @RequestParam String status) {
        try {
            ToggleStatusResponse response = surveyService.toggleSurveyStatus(surveyId, status);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a survey by surveyId
     * @param surveyId SurveyId of the survey the host wants to delete
     * @return DeleteSurveyResponse SurveyId of the survey that was deleted
     */
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
