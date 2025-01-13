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
        if(!status.equals("LIVE") && ! status.equals("NOT-LIVE")) {
            throw new RuntimeException("Invalid status for survey ID: " + surveyId);
        }

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found for ID: " + surveyId));

        if(survey.getStatus().equals(status)) {
            throw new RuntimeException("Survey status will remain the sama");
        }

        if(status.equals("LIVE") && survey.getStatus().equals("NOT-LIVE")) {
            String surveyHash = UUIDUtil.generateSurveyHash(survey.getTitle(), surveyId);
            if(surveyHash == null) {
                throw new RuntimeException("Error creating access code for survey: " + surveyId);
            } else {
                logger.info("Created access code " + surveyHash + " for survey " + surveyId);
                survey.setAccessCode(surveyHash);
            }
        }

        survey.setStatus(status);
        surveyRepository.save(survey);
        if(Boolean.TRUE.equals(redisTemplate.hasKey(SURVEY_CACHE_PREFIX + surveyId))) {
            logger.info("Deleting survey: " + surveyId + " from cache");
            redisTemplate.delete(SURVEY_CACHE_PREFIX + surveyId);
        }
        return survey.getStatus();
    }

}
