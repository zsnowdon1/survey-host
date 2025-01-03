package com.voting.survey_host.service;

import com.voting.survey_host.entity.SurveyDTO;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public interface SurveyService {

    SurveyDTO createEmptySurvey(SurveyDTO survey);

    SurveyDTO setSurvey(SurveyDTO survey);

}
