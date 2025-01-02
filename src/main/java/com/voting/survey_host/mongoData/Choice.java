package com.voting.survey_host.mongoData;

import org.springframework.data.annotation.Id;

public class Choice {

    @Id
    private long choiceId;

    private String choiceText;

    public long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(long choiceId) {
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
