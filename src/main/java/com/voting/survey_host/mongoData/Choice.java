package com.voting.survey_host.mongoData;

public class Choice {

    private String choiceId;

    private String choiceText;

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "choiceId=" + choiceId +
                ", choiceText='" + choiceText + '\'' +
                '}';
    }
}
