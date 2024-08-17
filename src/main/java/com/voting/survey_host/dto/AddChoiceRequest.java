package com.voting.survey_host.dto;

public class AddChoiceRequest {

    private long questionId;

    private String newChoice;

    public AddChoiceRequest(long questionId, String newChoice) {
        this.questionId = questionId;
        this.newChoice = newChoice;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getNewChoice() {
        return newChoice;
    }

    public void setNewChoice(String newChoice) {
        this.newChoice = newChoice;
    }
}
