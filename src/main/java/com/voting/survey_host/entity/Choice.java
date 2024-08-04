package com.voting.survey_host.entity;

public class Choice {

    private int choiceId;

    private int surveyId;

    private int questionId;

    private String choice;

    public Choice(int choiceId, int surveyId, int questionId, String choice) {
        this.choiceId = choiceId;
        this.surveyId = surveyId;
        this.questionId = questionId;
        this.choice = choice;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
