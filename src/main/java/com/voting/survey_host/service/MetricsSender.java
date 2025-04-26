package com.voting.survey_host.service;

import com.voting.survey_host.entity.MetricData;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class MetricsSender {
    private final RestTemplate restTemplate;

    public MetricsSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 10000) // Every 10 seconds
    public void sendMetrics() {
        MetricData data = new MetricData("main-app", 5, 600.0);
        restTemplate.postForObject("http://localhost:8081/metrics", data, Void.class);
    }
}
