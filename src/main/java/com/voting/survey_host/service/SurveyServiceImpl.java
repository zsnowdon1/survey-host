package com.voting.survey_host.service;

import com.voting.survey_host.dao.SurveyDao;
import com.voting.survey_host.dto.StartSurveyRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.dto.CreateSurveyRequest;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyDao surveyDao;

    @Override
    public void createSurvey(CreateSurveyRequest request) {
        String username = request.getUsername();

        Survey survey = new Survey(request.getUsername(), request.getTitle());

        int surveyId =  surveyDao.createSurvey(survey);
        survey.setSurveyId(surveyId);

        for(Question question: request.getQuestionList()) {
            question.setSurveyId(surveyId);
            int questionId = surveyDao.createQuestion(question);
            question.setQuestionId(questionId);

            for(Choice choice: question.getChoices()) {
                choice.setQuestionId(questionId);
                int choiceId = surveyDao.createChoice(choice);
                choice.setChoiceId(choiceId);
            }
        }
    }

    @Override
    public List<Survey> getSurveysByHost(String hostname) {
        return surveyDao.getSurveysByHost(hostname);
    }

    @Override
    public void startSurvey(StartSurveyRequest request) {

    }

}
