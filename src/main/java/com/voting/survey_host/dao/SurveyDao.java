package com.voting.survey_host.dao;

import com.voting.survey_host.dto.AddQuestionRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.ChoiceMapping;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;

import java.util.List;
import java.util.Map;

public interface SurveyDao {

    long createEmptySurvey(String title);

    Survey createSurvey(Survey survey);

    Survey setSurvey(Survey survey);

    List<Survey> getSurveysByHost(String host);

    Survey getSurveyById(long surveyId);

    Question getQuestionById(long questionId);

    List<Question> getQuestionList(long surveyId);

    List<Choice> getChoiceList(long questionId);

    List<ChoiceMapping> getChoiceMappings(long surveyId);
}
