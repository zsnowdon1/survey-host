package com.voting.survey_host.entity;

public class Vote {
    private long choiceId;

    private long votes;

    public Vote(long choiceId, long votes) {
        this.choiceId = choiceId;
        this.votes = votes;
    }

    public long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(long choiceId) {
        this.choiceId = choiceId;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }
}
