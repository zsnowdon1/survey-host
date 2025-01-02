package com.voting.survey_host.entity;

public class ChoiceDTO {

    private Long choiceId;

    private Long questionId;

    private String choiceText;

    public ChoiceDTO(Long choiceId, Long questionId, String choiceText) {
        this.choiceId = choiceId;
        this.questionId = questionId;
        this.choiceText = choiceText;
    }

    public ChoiceDTO() { }

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