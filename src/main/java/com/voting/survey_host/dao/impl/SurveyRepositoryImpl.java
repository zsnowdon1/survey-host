package com.voting.survey_host.dao.impl;

import com.voting.survey_host.dao.SurveyRepository;
import com.voting.survey_host.entity.SurveyDetailDTO;
import com.voting.survey_host.mongoData.Survey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class SurveyRepositoryImpl implements SurveyRepository {

    private final MongoTemplate mongoTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SurveyRepositoryImpl.class);

    public SurveyRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Survey insert(Survey survey) {
        return mongoTemplate.insert(survey);
    }

    @Override
    public Survey save(Survey survey) {
        return null;
    }

    @Override
    public List<SurveyDetailDTO> findSurveyDetailsByHostUsername(String hostUsername) {
        logger.info("Looking for survey data");
        MatchOperation matchStage = Aggregation.match(new Criteria("hostUsername").is(hostUsername));
        ProjectionOperation projectStage = Aggregation.project("title", "surveyId", "questions")
                .and(ArrayOperators.Size.lengthOfArray("questions")).as("questionCount");

        Aggregation aggregation = Aggregation.newAggregation(matchStage, projectStage);
        AggregationResults<SurveyDetailDTO> results = mongoTemplate.aggregate(aggregation, "surveys", SurveyDetailDTO.class);
        return results.getMappedResults();
    }

    @Override
    public List<Survey> findByHostUsername(String hostUsername) {
        return null;
    }

    @Override
    public <S extends Survey> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Survey> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Survey> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Survey> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Survey> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Survey> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Survey> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Survey, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Survey> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Survey> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Survey> findAll() {
        return null;
    }

    @Override
    public List<Survey> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Survey entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Survey> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Survey> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Survey> findAll(Pageable pageable) {
        return null;
    }
}
