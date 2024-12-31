package com.voting.survey_host.dao;

import com.voting.survey_host.mongoData.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveyRepository extends MongoRepository<Survey, String> {
}
