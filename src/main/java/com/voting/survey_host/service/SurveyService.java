package com.voting.survey_host.service;

import com.voting.survey_host.dto.CreateSurveyRequest;
import com.voting.survey_host.dto.StartSurveyRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SurveyService {

    void createSurvey(CreateSurveyRequest request);

    List<Survey> getSurveysByHost(String hostname);

    void startSurvey(StartSurveyRequest request);

    Survey getSurveyById(Long id);

    List<Question> getQuestionsBySurvey(Long surveyId);

    List<Choice> getChoicesByQuestion(Long questionId);

}
