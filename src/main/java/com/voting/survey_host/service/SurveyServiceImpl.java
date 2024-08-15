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
            Long questionId = surveyDao.createQuestion(question);
            question.setQuestionId(questionId);

            for(Choice choice: question.getChoices()) {
                choice.setQuestionId(questionId);
                Long choiceId = surveyDao.createChoice(choice);
                choice.setChoiceId(choiceId);
            }
        }
    }

    @Override
    public List<Survey> getSurveysByHost(String hostname) {
//        logger.info("RE");
        return surveyDao.getSurveysByHost(hostname);
    }

    @Override
    public void startSurvey(StartSurveyRequest request) {

    }

    @Override
    public Survey getSurveyById(Long id) {
        return surveyDao.getSurveyById(id);
    }

    @Override
    public List<Question> getQuestionsBySurvey(Long surveyId) {
        return surveyDao.getQuestionsBySurvey(surveyId);
    }

    @Override
    public List<Choice> getChoicesByQuestion(Long questionId) {
        return surveyDao.getChoicesByQuestion(questionId);
    }

}
