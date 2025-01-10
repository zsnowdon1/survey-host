package com.voting.survey_host.entity;

public class ChoiceDTO {

    private String choiceId;

    private String questionId;

    private String choiceText;

    public ChoiceDTO(String choiceId, String questionId, String choiceText) {
        this.choiceId = choiceId;
        this.questionId = questionId;
        this.choiceText = choiceText;
    }

    public ChoiceDTO() { }

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choice) {
        this.choiceText = choice;
    }

    @Override
    public String toString() {
        return "ChoiceDTO{" +
                "choiceId=" + choiceId +
                ", questionId=" + questionId +
                ", choiceText='" + choiceText + '\'' +
                '}';
    }
}