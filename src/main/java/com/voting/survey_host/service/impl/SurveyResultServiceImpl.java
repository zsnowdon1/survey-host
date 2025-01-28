package com.voting.survey_host.service.impl;

import com.voting.survey_host.service.SurveyResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SurveyResultServiceImpl implements SurveyResultService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SurveyResultServiceImpl.class);

    public SurveyResultServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Map<String, Map<String, Long>> getInitialResults(String surveyId) {
        Map<String, Map<String, Long>> results = new HashMap<>();
        logger.info("Fetching survey results for survey " + surveyId);
        Set<String> questionKeys = redisTemplate.keys("survey:" + surveyId + ":question:*:results");

        if(questionKeys != null) {
            for(String questionKey: questionKeys) {
                Map<Object, Object> choiceCounts = redisTemplate.opsForHash().entries(questionKey);
                Map<String, Long> votes = new HashMap<>();
                for(Map.Entry<Object, Object> entry: choiceCounts.entrySet()) {
                    String choiceId = entry.getKey().toString();
                    String voteCount = entry.getValue().toString();
                    votes.put(choiceId, Long.parseLong(voteCount));
                }

                String questionId = questionKey.split(":")[3];
                results.put(questionId, votes);
            }
        }
        return results;
    }
}
