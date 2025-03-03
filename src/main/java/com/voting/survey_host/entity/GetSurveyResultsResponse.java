package com.voting.survey_host.entity;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class GetSurveyResultsResponse {
    private Map<String, Map<String, Long>> resultMap;
}
