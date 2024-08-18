package com.voting.survey_host.dto;

import java.util.List;

public class AddQuestionRequest {

    private String title;

    private long surveyId;

    private List<String> choices;

    public AddQuestionRequest(String title, long surveyId, List<String> choices) {
        this.title = title;
        this.surveyId = surveyId;
        this.choices = choices;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }
}
