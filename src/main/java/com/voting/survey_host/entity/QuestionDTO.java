package com.voting.survey_host.entity;

import java.util.List;

public class QuestionDTO {

    private Long questionId;

    private Long surveyId;

    private String questionText;

    private List<ChoiceDTO> choices;

    public QuestionDTO(Long questionId, Long surveyId, String questionText, List<ChoiceDTO> choices) {
        this.questionId = questionId;
        this.surveyId = surveyId;
        this.questionText = questionText;
        this.choices = choices;
    }

    public QuestionDTO() { }

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

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String question) {
        this.questionText = question;
    }

    public List<ChoiceDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceDTO> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "questionId=" + questionId +
                ", surveyId=" + surveyId +
                ", questionText='" + questionText + '\'' +
                ", choices=" + choices +
                '}';
    }
}
