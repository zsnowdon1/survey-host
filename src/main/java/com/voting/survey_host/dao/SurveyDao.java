package com.voting.survey_host.dao;

import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;

public interface SurveyDao {

    int createSurvey(Survey request);

    int createQuestion(Question request);

    int createChoice(Choice choice);
}
