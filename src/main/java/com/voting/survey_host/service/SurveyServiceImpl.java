package com.voting.survey_host.service;

import com.voting.survey_host.dao.SurveyDao;
import com.voting.survey_host.dto.StartSurveyRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.dto.CreateSurveyRequest;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyDao surveyDao;

    private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

    @Override
    public void createSurvey(CreateSurveyRequest request) {
        String username = request.getUsername();

        Survey survey = new Survey(request.getUsername(), request.getTitle());

        Long surveyId =  surveyDao.createSurvey(survey);
        survey.setSurveyId(surveyId);

        for(Question question: request.getQuestionList()) {
            question.setSurveyId(surveyId);
            Long questionId = surveyDao.addQuestion(question);
            question.setQuestionId(questionId);
        }
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
    public Choice addChoice(long questionId, String newChoice) {
        Choice choice = new Choice();
        choice.setQuestionId(questionId);
        choice.setChoice(newChoice);
        long choiceId = surveyDao.addChoice(choice);
        choice.setChoiceId(choiceId);
        return choice;
    }

    @Override
    public Question addQuestion(Question newQuestion) {
        long questionId = surveyDao.addQuestion(newQuestion);
        newQuestion.setQuestionId(questionId);
        return newQuestion;
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
