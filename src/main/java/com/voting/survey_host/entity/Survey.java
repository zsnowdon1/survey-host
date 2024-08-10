package com.voting.survey_host.entity;

import java.util.List;

public class Survey {

    private Long surveyId;

    private String hostUsername;

    private String title;

    private List<Question> questionList;

    public Survey(Long surveyId, String hostUsername, String title, List<Question> questionList) {
        this.surveyId = surveyId;
        this.hostUsername = hostUsername;
        this.title = title;
        this.questionList = questionList;
    }

    public Survey(Long surveyId, String hostUsername, String title) {
        this.surveyId = surveyId;
        this.hostUsername = hostUsername;
        this.title = title;
    }

    public Survey(String hostUsername, String title) {
        this.hostUsername = hostUsername;
        this.title = title;
    }

    public Survey() { }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
}
