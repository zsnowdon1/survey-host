package com.voting.survey_host.entity;

public class ChoiceDTO {

    private String choiceId;

    private String choiceText;

    public ChoiceDTO(String choiceId, String choiceText) {
        this.choiceId = choiceId;
        this.choiceText = choiceText;
    }

    public ChoiceDTO() { }

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
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
                "choiceId='" + choiceId + '\'' +
                ", choiceText='" + choiceText + '\'' +
                '}';
    }
}