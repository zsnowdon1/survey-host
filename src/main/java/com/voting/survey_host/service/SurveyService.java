package com.voting.survey_host.service;

import com.voting.survey_host.entity.SurveyDTO;
import com.voting.survey_host.entity.SurveyDetailDTO;
import com.voting.survey_host.mongoData.Survey;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SurveyService {

    SurveyDTO createEmptySurvey(SurveyDTO survey);

    SurveyDTO setSurvey(SurveyDTO survey);

    List<SurveyDetailDTO> getSurveyDetailsByHostUsername(String hostUsername);

    void deleteSurvey(String surveyId);

}
