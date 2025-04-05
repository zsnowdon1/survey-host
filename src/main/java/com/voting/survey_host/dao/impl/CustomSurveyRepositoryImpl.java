package com.voting.survey_host.dao.impl;

import com.voting.entities.SurveyDetailDTO;
import com.voting.survey_host.dao.CustomSurveyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class CustomSurveyRepositoryImpl implements CustomSurveyRepository {

    private final MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CustomSurveyRepositoryImpl.class);

    public CustomSurveyRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<SurveyDetailDTO> findSurveyDetailsByHostUsername(String hostUsername) {
        logger.info("Looking for survey data for host: {}", hostUsername);
        MatchOperation matchStage = Aggregation.match(Criteria.where("hostUsername").is(hostUsername));
        ProjectionOperation projectStage = Aggregation.project("title", "surveyId", "questions", "status", "accessCode")
                .andExpression("size(ifNull(questions, []))").as("questionCount");

        Aggregation aggregation = Aggregation.newAggregation(matchStage, projectStage);
        logger.debug("Executing aggregation: {}", aggregation);
        AggregationResults<SurveyDetailDTO> results = mongoTemplate.aggregate(aggregation, "surveys", SurveyDetailDTO.class);
        List<SurveyDetailDTO> mappedResults = results.getMappedResults();
        logger.info("Found {} survey details for host: {}", mappedResults.size(), hostUsername);
        return mappedResults;
    }

}
