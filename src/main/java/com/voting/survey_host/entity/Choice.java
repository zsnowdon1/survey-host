package com.voting.survey_host.entity;

public class Choice {

    private int choiceId;

    private int questionId;

    private String choice;

    public Choice(int choiceId, int questionId, String choice) {
        this.choiceId = choiceId;
        this.questionId = questionId;
        this.choice = choice;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
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
