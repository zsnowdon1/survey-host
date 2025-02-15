package com.voting.survey_host.service;

import com.voting.entities.SurveyDTO;
import com.voting.entities.SurveyDetailDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SurveyService {

    SurveyDTO createSurvey(SurveyDTO survey);

    SurveyDTO setSurvey(SurveyDTO survey);

    SurveyDTO getSurvey(String surveyId);

    List<SurveyDetailDTO> getSurveyDetailsByHostUsername(String hostUsername);

    void deleteSurvey(String surveyId);

    String toggleSurveyStatus(String surveyId, String status);

}
