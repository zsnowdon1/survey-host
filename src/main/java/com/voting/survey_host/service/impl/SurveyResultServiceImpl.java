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

//    @Override
//    public List<QuestionVotes> getInitialResults(String surveyId) {
//        List<QuestionVotes> results = new ArrayList<>();
//        logger.info("Fetching survey results for survey " + surveyId);
//        Set<String> questionKeys = redisTemplate.keys("survey:" + surveyId + ":question:*:results");
//
//        if(questionKeys != null) {
//            for(String questionKey: questionKeys) {
//                Map<Object, Object> choiceCounts = redisTemplate.opsForHash().entries(questionKey);
//                List<Vote> votes = new ArrayList<>();
//                for(Map.Entry<Object, Object> entry: choiceCounts.entrySet()) {
//                    String choiceId = entry.getKey().toString();
//                    String voteCount = entry.getValue().toString();
//                    votes.add(new Vote(Long.parseLong(choiceId), Long.parseLong(voteCount)));
//                }
//
//                String questionId = questionKey.split(":")[3];
//                results.add(new QuestionVotes(questionId, votes));
//            }
//        }
//        return results;
//    }
}
