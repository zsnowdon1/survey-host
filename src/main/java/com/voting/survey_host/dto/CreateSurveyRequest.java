package com.voting.survey_host.dto;

import com.voting.survey_host.entity.Question;

import java.util.List;

public class CreateSurveyRequest {

    private String username;

    private String title;

    public CreateSurveyRequest(String username, String title) {
        this.username = username;
        this.title = title;
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
}
