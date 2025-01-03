package com.voting.survey_host.entity;

import java.util.List;

public class SurveyDTO {

    private String surveyId;

    private String hostUsername;

    private String title;

    private List<QuestionDTO> questions;

    public SurveyDTO(String surveyId, String hostUsername, String title, List<QuestionDTO> questionList) {
        this.surveyId = surveyId;
        this.hostUsername = hostUsername;
        this.title = title;
        this.questions = questionList;
    }

    public SurveyDTO() { }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
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

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "SurveyDTO{" +
                "surveyId='" + surveyId + '\'' +
                ", hostUsername='" + hostUsername + '\'' +
                ", title='" + title + '\'' +
                ", questions=" + questions +
                '}';
    }
}
