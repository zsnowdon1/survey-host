package com.voting.survey_host.dao;

import com.voting.mongoData.Survey;
import com.voting.survey_host.entity.ToggleStatusResponse;
import com.voting.survey_host.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.voting.survey_host.entity.Constants.LIVE;
import static com.voting.survey_host.entity.Constants.NOT_LIVE;

/**
 *
 */
@Component
public class SurveyStateManager {
    private final SurveyRepository surveyRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REDIS_SURVEY_CACHE_PREFIX = "survey:cache:";
    private static final Logger logger = LoggerFactory.getLogger(SurveyStateManager.class);

    public SurveyStateManager(SurveyRepository surveyRepository, RedisTemplate<String, Object> redisTemplate) {
        this.surveyRepository = surveyRepository;
        this.redisTemplate = redisTemplate;
    }

    public void clearCacheIfExists(String surveyId) {
        if(Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_SURVEY_CACHE_PREFIX + surveyId))) {
            logger.info("Deleting survey: " + surveyId + " from cache");
            redisTemplate.delete(REDIS_SURVEY_CACHE_PREFIX + surveyId);
        }
    }

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

    public ToggleStatusResponse toggleSurveyStatus(String surveyId, String status) {
        logger.info("Received request to update survey status to {} for survey ID: {}", status, surveyId);
        validateStatus(status, surveyId);

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found for ID: " + surveyId));
        if (survey.getStatus().equals(status)) {
            throw new RuntimeException("Survey status is already " + status + " for ID: " + surveyId);
        }

        survey.setUpdatedAt(LocalDateTime.now().toString());
        updateSurveyState(survey, status);

        surveyRepository.save(survey);
        return new ToggleStatusResponse(survey.getStatus(), survey.getAccessCode());
    }

    private void validateStatus(String status, String surveyId) {
        if (!status.equals(LIVE) && !status.equals(NOT_LIVE)) {
            throw new RuntimeException("Invalid status '" + status + "' for survey ID: " + surveyId);
        }
    }

    private void updateSurveyState(Survey survey, String newStatus) {
        String currentStatus = survey.getStatus();
        if (newStatus.equals(LIVE) && currentStatus.equals(NOT_LIVE)) {
            switchToLive(survey);
        } else if (newStatus.equals(NOT_LIVE) && currentStatus.equals(LIVE)) {
            switchToNotLive(survey);
        }
    }

    private void switchToLive(Survey survey) {
        survey.setStatus(LIVE);
        String surveyHash = UUIDUtil.generateSurveyHash(survey.getTitle(), survey.getSurveyId());
        if (surveyHash == null) {
            throw new RuntimeException("Error creating access code for survey: " + survey.getSurveyId());
        }
        survey.setAccessCode(surveyHash);
        logger.info("Adding survey {} to Redis cache with hash {}", survey.getSurveyId(), surveyHash);
        redisTemplate.opsForValue().set(REDIS_SURVEY_CACHE_PREFIX + surveyHash, survey);
    }

    private void switchToNotLive(Survey survey) {
        logger.info("Removing survey {} from Redis cache", survey.getSurveyId());
        redisTemplate.delete(REDIS_SURVEY_CACHE_PREFIX + survey.getAccessCode());
        survey.setStatus(NOT_LIVE);
        survey.setAccessCode(null);
    }
}
