package com.voting.survey_host.dao;

import com.voting.survey_host.entity.SurveyDetailDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomSurveyRepository {
    List<SurveyDetailDTO> findSurveyDetailsByHostUsername(String hostUsername);
}
