package com.voting.survey_host.service;

import com.voting.survey_host.dao.SurveyDaoImpl;
import com.voting.survey_host.dto.AddQuestionRequest;
import com.voting.survey_host.dto.StartSurveyRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.ChoiceMapping;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyDaoImpl surveyDao;

    private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

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

    @Override
    public long addChoice(long questionId, String newChoice) {
        Choice choice = new Choice();
        choice.setQuestionId(questionId);
        choice.setChoice(newChoice);
        return surveyDao.addChoice(choice);
    }

    @Override
    public long addQuestion(AddQuestionRequest newQuestion) {
        return surveyDao.addQuestion(newQuestion);
    }

    @Override
    public int deleteSurvey(long surveyId) {
        return surveyDao.deleteSurvey(surveyId);
    }

    @Override
    public int deleteChoice(long choiceId) {
        return surveyDao.deleteChoice(choiceId);
    }

    @Override
    public int deleteQuestion(long questionId) {
        return surveyDao.deleteQuestion(questionId);
    }

}
