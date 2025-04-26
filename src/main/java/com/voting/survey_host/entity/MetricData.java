package com.voting.survey_host.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class MetricData {
    private String serviceName;
    private int errorCount;
    private double latency;
}
