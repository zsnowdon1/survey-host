package com.voting.survey_host.service;

import com.voting.survey_host.controller.LiveVoteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class SurveyResultServiceImpl implements SurveyResultService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SurveyResultServiceImpl.class);

    @Override
    public Map<String, Map<String, Long>> getInitialResults(String surveyId) {
        Map<String, Map<String, Long>> results = new HashMap<>();
        logger.info("Fetching survey results for survey " + surveyId);
        Set<String> questionKeys = redisTemplate.keys("survey:" + surveyId + ":question:*:results");

        if(questionKeys != null) {
            for(String questionKey: questionKeys) {
                Map<Object, Object> choiceCounts = redisTemplate.opsForHash().entries(questionKey);
                Map<String, Long> questionResult = new HashMap<>();
                for(Map.Entry<Object, Object> entry: choiceCounts.entrySet()) {
                    questionResult.put((String) entry.getKey(), Long.parseLong(entry.getValue().toString()));
                }
                String questionId = questionKey.split(":")[3];
                results.put(questionId, questionResult);
            }
        }
        return results;
    }
}
