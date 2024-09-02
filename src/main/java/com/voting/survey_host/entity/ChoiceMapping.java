package com.voting.survey_host.entity;

public class ChoiceMapping {
    private long choiceId;
    private String choiceName;

    public ChoiceMapping(long choiceId, String choiceName) {
        this.choiceId = choiceId;
        this.choiceName = choiceName;
    }

    public long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(long choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoiceName() {
        return choiceName;
    }

    public void setChoiceName(String choiceName) {
        this.choiceName = choiceName;
    }
}
