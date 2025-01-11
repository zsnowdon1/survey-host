package com.voting.survey_host.service.impl;

import com.voting.entities.SurveyDTO;
import com.voting.entities.SurveyDetailDTO;
import com.voting.mongoData.Survey;
import com.voting.survey_host.dao.CustomSurveyRepository;
import com.voting.survey_host.dao.SurveyRepository;
import com.voting.survey_host.service.SurveyService;
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

}
