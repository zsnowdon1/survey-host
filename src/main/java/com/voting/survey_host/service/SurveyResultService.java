package com.voting.survey_host.service;

import com.voting.survey_host.entity.GetSurveyResultsResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SurveyResultService {

    List<GetSurveyResultsResponse> getInitialResults(String surveyId);
}
