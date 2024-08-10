package com.voting.survey_host.dao;

import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import com.voting.survey_host.rowMapper.SurveyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class SurveyDaoImpl implements SurveyDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String createSurveyQuery = "INSERT INTO survey (host_username, title) VALUES (?,?)";
    private final String createQuestionQuery = "INSERT INTO question (survey_id, question) VALUES (?,?)";
    private final String createChoiceQuery = "INSERT INTO choice (question_id, choice_text) VALUES (?,?)";
    private final String getSurveysByHostQuery = "SELECT survey_id, title from survey where host_username=?";

    @Override
    public int createSurvey(Survey request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createSurveyQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getHostUsername());
            ps.setString(2, request.getTitle());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public int createQuestion(Question request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createQuestionQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, request.getSurveyId());
            ps.setString(2, request.getQuestion());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public List<Survey> getSurveysByHost(String hostname) {
        List<Survey> surveys;
        try {
            return jdbcTemplate.query(getSurveysByHostQuery, new SurveyRowMapper(hostname), hostname);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int createChoice(Choice request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createChoiceQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, request.getQuestionId());
            ps.setString(2, request.getChoice());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }
}
