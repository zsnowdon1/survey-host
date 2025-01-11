package com.voting.survey_host.dao;

import com.voting.entities.SurveyDetailDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomSurveyRepository {
    List<SurveyDetailDTO> findSurveyDetailsByHostUsername(String hostUsername);
}
