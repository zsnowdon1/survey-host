package com.voting.survey_host.entity;

import java.util.List;

public class Question {

    private Long questionId;

    private Long surveyId;

    private String question;

    private List<Choice> choices;

    public Question(Long questionId, Long surveyId, String question, List<Choice> choices) {
        this.questionId = questionId;
        this.surveyId = surveyId;
        this.question = question;
        this.choices = choices;
    }

    public Question() { }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
