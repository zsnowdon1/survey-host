package com.voting.survey_host.dto;

import com.voting.survey_host.entity.Question;

import java.util.List;

public class CreateSurveyRequest {

    private String username;

    private String title;

    private List<Question> questionList;

    public CreateSurveyRequest(String username, String title, List<Question> questionList) {
        this.username = username;
        this.title = title;
        this.questionList = questionList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
