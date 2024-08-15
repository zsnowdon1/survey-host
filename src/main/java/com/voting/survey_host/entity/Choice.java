package com.voting.survey_host.entity;

public class Choice {

    private Long choiceId;

    private Long questionId;

    private String choice;

    public Choice(Long choiceId, Long questionId, String choice) {
        this.choiceId = choiceId;
        this.questionId = questionId;
        this.choice = choice;
    }

    public Choice() { }

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
