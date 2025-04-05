package com.voting.survey_host.service;

import com.voting.entities.SurveyDTO;
import com.voting.entities.SurveyDetailDTO;
import com.voting.survey_host.entity.ToggleStatusResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SurveyService {
    SurveyDTO createSurvey(SurveyDTO survey);
    SurveyDTO setSurvey(SurveyDTO survey);
    SurveyDTO getSurvey(String surveyId);
    List<SurveyDetailDTO> getSurveyDetailsByHostUsername(String hostUsername);
    void deleteSurvey(String surveyId);
    ToggleStatusResponse toggleSurveyStatus(String surveyId, String status);
    Map<String, Map<String, Long>> getInitialResults(String surveyId);
}
