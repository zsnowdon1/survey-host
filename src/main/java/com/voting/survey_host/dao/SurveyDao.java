package com.voting.survey_host.dao;

import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;

import java.util.List;

public interface SurveyDao {

    long createSurvey(Survey request);

    long addQuestion(Question request);

    long addChoice(Choice choice);

    List<Survey> getSurveysByHost(String host);

    Survey getSurveyById(long surveyId);

    Question getQuestionById(long questionId);

    List<Question> getQuestionList(long surveyId);

    List<Choice> getChoiceList(long questionId);

    int deleteChoice(long choiceId);

    int deleteQuestion(long questionId);
}
