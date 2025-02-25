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
        logger.info("Looking for survey data");
        MatchOperation matchStage = Aggregation.match(new Criteria("hostUsername").is(hostUsername));
        ProjectionOperation projectStage = Aggregation.project("title", "surveyId", "questions", "status", "accessCode")
                .and(ArrayOperators.Size.lengthOfArray(
                        ConditionalOperators.ifNull("questions").then(Collections.emptyList())
                )).as("questionCount");

        Aggregation aggregation = Aggregation.newAggregation(matchStage, projectStage);
        AggregationResults<SurveyDetailDTO> results = mongoTemplate.aggregate(aggregation, "surveys", SurveyDetailDTO.class);
        return results.getMappedResults();
    }

}
