package com.voting.survey_host.utils;

import com.voting.mongoData.Choice;
import com.voting.mongoData.Question;
import com.voting.mongoData.Survey;
import java.util.UUID;

public class SurveyServiceHelper {

    public static void populateRandomIds(Survey survey) {
        for (Question question : survey.getQuestions()) {
            question.setQuestionId(UUID.randomUUID().toString());
            for (Choice choice : question.getChoices()) {
                choice.setChoiceId(UUID.randomUUID().toString());
            }
        }
    }
}
