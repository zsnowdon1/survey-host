package com.voting.survey_host.service.impl;

import com.voting.survey_host.dao.SurveyRepository;
import com.voting.survey_host.entity.SurveyDTO;
import com.voting.survey_host.entity.SurveyDetailDTO;
import com.voting.survey_host.mongoData.Survey;
import com.voting.survey_host.mongoData.SurveyMapper;
import com.voting.survey_host.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    private static final Logger logger = LoggerFactory.getLogger(SurveyServiceImpl.class);

    public SurveyServiceImpl(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Override
    public SurveyDTO createEmptySurvey(SurveyDTO surveyDTO) {
        Survey newSurvey = SurveyMapper.toEntitySurvey(surveyDTO);
        surveyRepository.insert(newSurvey);
        return SurveyMapper.toDTOSurvey(newSurvey);
    }

    @Override
    public SurveyDTO setSurvey(SurveyDTO surveyDTO) {
        Survey newSurvey = SurveyMapper.toEntitySurvey(surveyDTO);
        return surveyRepository.findById(newSurvey.getSurveyId())
                .map(existingSurvey -> {
                    newSurvey.setSurveyId(existingSurvey.getSurveyId());
                    surveyRepository.save(newSurvey);
                    return SurveyMapper.toDTOSurvey(newSurvey);
                })
                .orElseThrow(() -> new NoSuchElementException("Couldn't update survey"));
    }

    @Override
    public List<SurveyDTO> getSurveysByHostUsername(String hostUsername) {
        List<Survey> surveys = surveyRepository.findByHostUsername(hostUsername);
        return SurveyMapper.toDTOSurveyList(surveys);
    }

    @Override
    public List<SurveyDetailDTO> getSurveyDetailsByHostUsername(String hostUsername) {
        return surveyRepository.findSurveyDetailsByHostUsername(hostUsername);
    }

}
