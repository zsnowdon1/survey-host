package com.voting.survey_host.service.impl;

import com.voting.entities.SurveyDTO;
import com.voting.entities.SurveyDetailDTO;
import com.voting.mongoData.Survey;
import com.voting.survey_host.dao.CustomSurveyRepository;
import com.voting.survey_host.dao.SurveyRepository;
import com.voting.survey_host.service.SurveyService;
import com.voting.survey_host.utils.UUIDUtil;
import com.voting.utils.SurveyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.voting.survey_host.entity.Constants.LIVE;
import static com.voting.survey_host.entity.Constants.NOT_LIVE;

@Service
public class SurveyServiceImpl implements SurveyService {

    private final CustomSurveyRepository customSurveyRepository;

    private final SurveyRepository surveyRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SURVEY_CACHE_PREFIX = "Survey:";

    private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

    public SurveyServiceImpl(CustomSurveyRepository customSurveyRepository,
                             SurveyRepository surveyRepository,
                             RedisTemplate<String, Object>  redisTemplate) {
        this.customSurveyRepository = customSurveyRepository;
        this.surveyRepository = surveyRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public SurveyDTO createSurvey(SurveyDTO surveyDTO) {
        Survey newSurvey = SurveyMapper.toEntitySurvey(surveyDTO);
        surveyRepository.insert(newSurvey);
        return SurveyMapper.toDTOSurvey(newSurvey);
    }

    @Override
    public SurveyDTO setSurvey(SurveyDTO surveyDTO) {
        Survey newSurvey = SurveyMapper.toEntitySurvey(surveyDTO);
        if(Boolean.TRUE.equals(redisTemplate.hasKey(SURVEY_CACHE_PREFIX + surveyDTO.getSurveyId()))) {
            logger.info("Deleting survey: " + surveyDTO.getSurveyId() + " from cache");
            redisTemplate.delete(SURVEY_CACHE_PREFIX + surveyDTO.getSurveyId());
        }
        return surveyRepository.findById(newSurvey.getSurveyId())
                .map(existingSurvey -> {
                    newSurvey.setSurveyId(existingSurvey.getSurveyId());
                    surveyRepository.save(newSurvey);
                    return SurveyMapper.toDTOSurvey(newSurvey);
                })
                .orElseThrow(() -> new NoSuchElementException("Couldn't update survey"));
    }

    @Override
    public List<SurveyDetailDTO> getSurveyDetailsByHostUsername(String hostUsername) {
        return customSurveyRepository.findSurveyDetailsByHostUsername(hostUsername);
    }

    @Override
    public void deleteSurvey(String surveyId) {
        surveyRepository.deleteById(surveyId);
    }

    @Override
    public String toggleSurveyStatus(String surveyId, String status) {
        logger.info("Received request to updates survey status to " + status + " for survey ID: " + surveyId);
        if(!status.equals(LIVE) && ! status.equals(NOT_LIVE)) {
            throw new RuntimeException("Invalid status for survey ID: " + surveyId);
        }

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found for ID: " + surveyId));
        if(survey.getStatus().equals(status)) {
            throw new RuntimeException("Survey status will remain the same");
        }

        // If switching to LIVE, create survey hash
        // If switching to NOT-LIVE, remove hash
        if(status.equals(LIVE) && survey.getStatus().equals(NOT_LIVE)) {
            survey.setStatus(status);
            String surveyHash = UUIDUtil.generateSurveyHash(survey.getTitle(), surveyId);
            if(surveyHash == null) {
                throw new RuntimeException("Error creating access code for survey: " + surveyId);
            } else {
                survey.setAccessCode(surveyHash);
                logger.info("Adding survey: " + surveyId + " to cache");
                redisTemplate.opsForValue().set(SURVEY_CACHE_PREFIX + surveyHash, survey);
            }
        } else if(status.equals(NOT_LIVE) && survey.getStatus().equals(LIVE)) {
            logger.info("Deleting survey: " + surveyId + " from cache");
            survey.setStatus(status);
            redisTemplate.delete(SURVEY_CACHE_PREFIX + survey.getAccessCode());
            survey.setAccessCode(null);
        }

        surveyRepository.save(survey);
        return survey.getStatus();
    }

}
