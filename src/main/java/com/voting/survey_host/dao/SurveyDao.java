package com.voting.survey_host.dao;

import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;

import java.util.List;

public interface SurveyDao {

    Long createSurvey(Survey request);

    Long createQuestion(Question request);

    List<Survey> getSurveysByHost(String host);

    Survey getSurveyById(Long id);

    Long createChoice(Choice choice);

    List<Question> getQuestionsBySurvey(Long surveyId);

    List<Choice> getChoicesByQuestion(Long questionId);
}
