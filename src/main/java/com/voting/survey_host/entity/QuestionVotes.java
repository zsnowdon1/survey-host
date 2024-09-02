package com.voting.survey_host.entity;

import java.util.List;

public class QuestionVotes {

    private String questionId;

    private List<Vote> votes;

    public QuestionVotes(String questionId, List<Vote> votes) {
        this.questionId = questionId;
        this.votes = votes;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
