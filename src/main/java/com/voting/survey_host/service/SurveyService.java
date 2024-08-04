package com.voting.survey_host.service;

import com.voting.survey_host.dto.CreateSurveyRequest;
import com.voting.survey_host.dto.StartSurveyRequest;
import org.springframework.stereotype.Service;

@Service
public interface SurveyService {

    void createSurvey(CreateSurveyRequest request);

    void startSurvey(StartSurveyRequest request);

}
