package com.voting.survey_host.dao;

import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import com.voting.survey_host.resultSetExtractor.SurveyResultSetExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import static com.voting.survey_host.entity.DBConstants.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class SurveyDaoImpl implements SurveyDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SurveyDaoImpl.class);

    @Override
    public long createSurvey(Survey request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createSurveyQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getHostUsername());
            ps.setString(2, request.getTitle());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public long createQuestion(Question request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createQuestionQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, request.getSurveyId());
            ps.setString(2, request.getQuestion());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public long addChoice(Choice request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createChoiceQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, request.getQuestionId());
            ps.setString(2, request.getChoice());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Survey> getSurveysByHost(String hostname) {
        return jdbcTemplate.query(getSurveysByHostQuery, new RowMapper<Survey>() {
            @Override
            public Survey mapRow(ResultSet rs, int rowNum) throws SQLException {
                Survey survey = new Survey();
                survey.setSurveyId(rs.getLong("survey_id"));
                survey.setHostUsername(hostname);
                survey.setTitle(rs.getString("title"));
                return survey;
            }
        }, hostname);
    }

    @Override
    public Survey getSurveyById(long surveyId) {
        return jdbcTemplate.query(getSurveyById, new SurveyResultSetExtractor(), surveyId);
    }

    @Override
    public Question getQuestionById(long questionId) {
        return jdbcTemplate.queryForObject(getQuestionByIdQuery, new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
                Question question = new Question();
                question.setQuestionId(questionId);
                question.setSurveyId(rs.getLong("survey_id"));
                question.setQuestion(rs.getString("question"));
                return question;
            }
        }, questionId);
    }

    @Override
    public List<Question> getQuestionList(long surveyId) {
        return jdbcTemplate.query(getQuestionsBySurveyQuery, new RowMapper<Question>() {
            @Override
            public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
                Question question = new Question();
                question.setQuestionId(rs.getLong("question_id"));
                question.setSurveyId(surveyId);
                question.setQuestion(rs.getString("question"));
                question.setChoices(new ArrayList<>());
                return question;
            }
        }, surveyId);
    }

    @Override
    public List<Choice> getChoiceList(long questionId) {
        return jdbcTemplate.query(getChoicesByQuestionQuery, new RowMapper<Choice>() {
            @Override
            public Choice mapRow(ResultSet rs, int rowNum) throws SQLException {
                Choice choice = new Choice();
                choice.setChoiceId(rs.getLong("choice_id"));
                choice.setQuestionId(questionId);
                choice.setChoice(rs.getString("choice"));
                return choice;
            }
        }, questionId);
    }

    @Override
    public long deleteChoice(long choiceId) {
        return jdbcTemplate.update(deleteChoiceQuery, choiceId);
    }
}
