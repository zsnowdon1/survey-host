package com.voting.survey_host.entity;

import java.util.List;

public class Question {

    private int questionId;

    private int surveyId;

    private String question;

    private List<Choice> choices;

    public Question(int questionId, int surveyId, String question, List<Choice> choices) {
        this.questionId = questionId;
        this.surveyId = surveyId;
        this.question = question;
        this.choices = choices;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
