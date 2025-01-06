package com.voting.survey_host.dao;

import com.voting.survey_host.entity.SurveyDetailDTO;
import com.voting.survey_host.mongoData.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends MongoRepository<Survey, String> {

    Survey insert(Survey survey);

    Survey save(Survey survey);

    List<SurveyDetailDTO> findSurveyDetailsByHostUsername(String hostUsername);

    List<Survey> findByHostUsername(String hostUsername);
}
