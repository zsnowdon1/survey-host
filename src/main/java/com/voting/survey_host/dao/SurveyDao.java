package com.voting.survey_host.dao;

import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;

import java.util.List;

public interface SurveyDao {

    int createSurvey(Survey request);

    int createQuestion(Question request);

    List<Survey> getSurveysByHost(String host);

    int createChoice(Choice choice);
}
