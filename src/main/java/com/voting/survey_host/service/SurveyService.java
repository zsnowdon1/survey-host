package com.voting.survey_host.service;

import com.voting.survey_host.dto.AddQuestionRequest;
import com.voting.survey_host.dto.CreateSurveyRequest;
import com.voting.survey_host.dto.StartSurveyRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SurveyService {

    long createEmptySurvey(String title);

    List<Survey> getSurveysByHost(String hostname);

    void startSurvey(StartSurveyRequest request);

    Survey getSurveyById(long surveyId);

    Question getQuestionById(long questionId);

    List<Question> getQuestionList(long surveyId);

    List<Choice> getChoiceList(long questionId);

    long addChoice(long questionId, String newChoice);

    long addQuestion(AddQuestionRequest newQuestion);

    int deleteSurvey(long surveyId);

    int deleteChoice(long choiceId);

    int deleteQuestion(long questionId);

}
