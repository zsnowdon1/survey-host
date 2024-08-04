package com.voting.survey_host.entity;

public class Survey {

    private int surveyId;

    private String hostUsername;

    private String title;

    public Survey(int surveyId, String hostUsername, String title) {
        this.surveyId = surveyId;
        this.hostUsername = hostUsername;
        this.title = title;
    }

    public Survey(String hostUsername, String title) {
        this.hostUsername = hostUsername;
        this.title = title;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
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
}
