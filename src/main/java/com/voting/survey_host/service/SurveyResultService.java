package com.voting.survey_host.service;

import com.voting.survey_host.entity.QuestionVotes;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SurveyResultService {

    List<QuestionVotes> getInitialResults(String surveyId);
}
