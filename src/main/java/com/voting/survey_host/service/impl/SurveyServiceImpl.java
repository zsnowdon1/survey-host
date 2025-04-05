package com.voting.survey_host.service.impl;

import com.voting.entities.SurveyDTO;
import com.voting.entities.SurveyDetailDTO;
import com.voting.mongoData.Survey;
import com.voting.survey_host.dao.CustomSurveyRepository;
import com.voting.survey_host.dao.SurveyRepository;
import com.voting.survey_host.dao.SurveyStateManager;
import com.voting.survey_host.entity.ToggleStatusResponse;
import com.voting.survey_host.service.SurveyService;
import com.voting.utils.SurveyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class SurveyServiceImpl implements SurveyService {

    private final CustomSurveyRepository customSurveyRepository;

    private final SurveyRepository surveyRepository;

    private final SurveyStateManager surveyStateManager;

    private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

    public SurveyServiceImpl(CustomSurveyRepository customSurveyRepository,
                             SurveyRepository surveyRepository,
                             SurveyStateManager surveyStateManager) {
        this.customSurveyRepository = customSurveyRepository;
        this.surveyRepository = surveyRepository;
        this.surveyStateManager = surveyStateManager;
    }

    @Override
    public SurveyDTO createSurvey(SurveyDTO surveyDTO) {
        logger.info("Creating survey for host: {}", surveyDTO.getHostUsername());
        Survey newSurvey = SurveyMapper.mapToMongo(surveyDTO);
        newSurvey.setCreatedAt(LocalDateTime.now().toString());
        newSurvey.setUpdatedAt(LocalDateTime.now().toString());
        newSurvey.setStatus("NOT-LIVE");
        surveyRepository.insert(newSurvey);
        logger.info("Survey created with ID: {}", newSurvey.getSurveyId());
        return SurveyMapper.mapToDTO(newSurvey);
    }

    @Override
    public SurveyDTO setSurvey(SurveyDTO surveyDTO) {
        Survey newSurvey = SurveyMapper.mapToMongo(surveyDTO);
        newSurvey.setUpdatedAt(LocalDateTime.now().toString());
        return surveyRepository.findById(newSurvey.getSurveyId())
                .map(existingSurvey -> {
                    newSurvey.setSurveyId(existingSurvey.getSurveyId());
                    surveyRepository.save(newSurvey);
                    surveyStateManager.clearCacheIfExists(newSurvey.getSurveyId());
                    return SurveyMapper.mapToDTO(newSurvey);
                })
                .orElseThrow(() -> new NoSuchElementException("Survey not found: " + newSurvey.getSurveyId()));
    }

    @Override
    public SurveyDTO getSurvey(String surveyId) {
        logger.info("Fetching survey with ID: {}", surveyId);
        return surveyRepository.findById(surveyId)
                .map(SurveyMapper::mapToDTO).orElseThrow(() -> new NoSuchElementException("Survey not found: " + surveyId));
    }

    @Override
    public List<SurveyDetailDTO> getSurveyDetailsByHostUsername(String hostUsername) {
        logger.info("Fetching survey details for host: {}", hostUsername);
        return customSurveyRepository.findSurveyDetailsByHostUsername(hostUsername);
    }

    @Override
    public void deleteSurvey(String surveyId) {
        logger.info("Deleting survey with ID: {}", surveyId);
        surveyRepository.deleteById(surveyId);
    }

    @Override
    public Map<String, Map<String, Long>> getInitialResults(String surveyId) {
        logger.info("Fetching initial results for survey: {}", surveyId);
        return surveyStateManager.getInitialResults(surveyId);
    }

    @Override
    public ToggleStatusResponse toggleSurveyStatus(String surveyId, String status) {
        logger.info("Toggling status for survey: {} to {}", surveyId, status);
        return surveyStateManager.toggleSurveyStatus(surveyId, status);
    }

}
