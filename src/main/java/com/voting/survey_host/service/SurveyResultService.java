package com.voting.survey_host.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SurveyResultService {

    Map<String, Map<String, Long>> getInitialResults(String surveyId);
}
