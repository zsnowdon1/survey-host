package com.voting.survey_host.service.impl;

import com.voting.survey_host.dao.impl.SurveyDaoImpl;
import com.voting.survey_host.dto.AddQuestionRequest;
import com.voting.survey_host.dto.StartSurveyRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.ChoiceMapping;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyDaoImpl surveyDao;

    private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

    public SurveyServiceImpl(SurveyDaoImpl surveyDao) {
        this.surveyDao = surveyDao;
    }

    @Override
    public long createEmptySurvey(String title) {
        return surveyDao.createEmptySurvey(title);
    }

    @Override
    public List<Survey> getSurveysByHost(String hostname) {
        return surveyDao.getSurveysByHost(hostname);
    }

    @Override
    public void startSurvey(StartSurveyRequest request) {

    }

    @Override
    public void setSurvey(Survey survey) {
        if(isNull(survey.getSurveyId())) {
            Survey output = surveyDao.createSurvey(survey);
        } else {
            Survey output = surveyDao.setSurvey(survey);
        }
    }

    @Override
    public Survey getSurveyById(long id) {
        return surveyDao.getSurveyById(id);
    }

    @Override
    public Question getQuestionById(long questionId) {
        return surveyDao.getQuestionById(questionId);
    }

    @Override
    public List<Question> getQuestionList(long surveyId) {
        return surveyDao.getQuestionList(surveyId);
    }

    @Override
    public List<Choice> getChoiceList(long questionId) {
        return surveyDao.getChoiceList(questionId);
    }

    @Override
    public List<ChoiceMapping> getChoicesBySurvey(long surveyId) {
        return surveyDao.getChoiceMappings(surveyId);
    }

}
