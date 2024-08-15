package com.voting.survey_host.dto;

import java.util.Map;

public class getSurveysByHostResponse {

    private String response;

    private Map<Long, String> surveyList;

    public getSurveysByHostResponse() { };

    public getSurveysByHostResponse(String response, Map<Long, String> surveyList) {
        this.response = response;
        this.surveyList = surveyList;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Map<Long, String> getSurveyList() {
        return surveyList;
    }

    public void setSurveyList(Map<Long, String> surveyList) {
        this.surveyList = surveyList;
    }
}
