package com.voting.survey_host.mongoData;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Question {

    @Id
    private Long questionId;
    private String questionText;

    private List<Choice> choices;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", questionText='" + questionText + '\'' +
                ", choices=" + choices +
                '}';
    }
}
