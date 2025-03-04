package com.voting.survey_host.controller;

import com.voting.entities.SurveyDTO;
import com.voting.entities.SurveyDetailDTO;
import com.voting.survey_host.entity.DeleteSurveyResponse;
import com.voting.survey_host.entity.ToggleStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/host/surveys")
@CrossOrigin
public interface SurveyHostController {

    @PostMapping()
    ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO surveyDTO);

    @PutMapping("/{surveyId}")
    ResponseEntity<SurveyDTO> setSurvey(@PathVariable String surveyId, @RequestBody SurveyDTO surveyDTO);

    @GetMapping("/{surveyId}")
    ResponseEntity<SurveyDTO> getSurvey(@PathVariable String surveyId);

    @GetMapping()
    ResponseEntity<List<SurveyDetailDTO>> getSurveyDetailsByHostname(@RequestParam("hostUsername") String hostUsername);

    @PutMapping("/{surveyId}/status")
    ResponseEntity<ToggleStatusResponse> toggleSurveyStatus(@PathVariable String surveyId, @RequestParam String status);

    @DeleteMapping("/{surveyId}")
    ResponseEntity<DeleteSurveyResponse> deleteSurvey(@PathVariable String surveyId);

}
