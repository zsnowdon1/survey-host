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

    Survey getSurveyById(long surveyId);

    Question getQuestionById(long questionId);

    List<Question> getQuestionList(long surveyId);

    List<Choice> getChoiceList(long questionId);

    Choice addChoice(long questionId, String newChoice);

    long deleteChoice(long choiceId);

}
