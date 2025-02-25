package com.voting.survey_host.entity;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class GetSurveyResultsResponse {

    private String questionId;
    private Map<String, Long> choices;
}
