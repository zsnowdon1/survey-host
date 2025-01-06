package com.voting.survey_host.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("survey")
public class SurveyDetailDTO {

    @Id
    private String surveyId;

    private String title;

    private int questionCount;

    public SurveyDetailDTO(String surveyId, String title, int questionCount) {
        this.surveyId = surveyId;
        this.title = title;
        this.questionCount = questionCount;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    @Override
    public String toString() {
        return "SurveyDetailDTO{" +
                "surveyId='" + surveyId + '\'' +
                ", title='" + title + '\'' +
                ", questionCount=" + questionCount +
                '}';
    }
}
