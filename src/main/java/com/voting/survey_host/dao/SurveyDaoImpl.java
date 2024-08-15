package com.voting.survey_host.dao;

import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.DBConstants;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import com.voting.survey_host.resultSetExtractor.SurveyResultSetExtractor;
import com.voting.survey_host.rowMapper.ChoiceRowMapper;
import com.voting.survey_host.rowMapper.QuestionRowMapper;
import com.voting.survey_host.rowMapper.SurveyRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import static com.voting.survey_host.entity.DBConstants.*;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class SurveyDaoImpl implements SurveyDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SurveyDaoImpl.class);

    @Override
    public Long createSurvey(Survey request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(DBConstants.createSurveyQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getHostUsername());
            ps.setString(2, request.getTitle());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Long createQuestion(Question request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(DBConstants.createQuestionQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, request.getSurveyId());
            ps.setString(2, request.getQuestion());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Survey> getSurveysByHost(String hostname) {
        try {
            return jdbcTemplate.query(DBConstants.getSurveysByHostQuery, new SurveyRowMapper(hostname), hostname);
        } catch (Exception e) {
            logger.info("Error getting survey by host");
            return null;
        }
    }

    @Override
    public Survey getSurveyById(Long id) {
        try {
            return jdbcTemplate.query(DBConstants.getSurveyById, new SurveyResultSetExtractor(), id);
        } catch (Exception e) {
            logger.info("Failed retrieving survey {}", id);
            throw e;
        }
    }

    @Override
    public Long createChoice(Choice request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(DBConstants.createChoiceQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, request.getQuestionId());
            ps.setString(2, request.getChoice());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Question> getQuestionsBySurvey(Long surveyId) {
        try {
            return jdbcTemplate.query(getQuestionsBySurveyQuery, new QuestionRowMapper(surveyId), surveyId);
        } catch (Exception e) {
            logger.info("Error getting survey by host");
            return null;
        }
    }

    @Override
    public List<Choice> getChoicesByQuestion(Long questionId) {
        try {
            return jdbcTemplate.query(getQuestionsBySurveyQuery, new ChoiceRowMapper(questionId), questionId);
        } catch (Exception e) {
            logger.info("Error getting survey by host");
            return null;
        }
    }
}
