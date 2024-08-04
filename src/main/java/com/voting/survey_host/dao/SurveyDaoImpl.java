package com.voting.survey_host.dao;

import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class SurveyDaoImpl implements SurveyDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String createSurveyQuery = "INSERT INTO survey (host_username, title) VALUES (?,?)";
    private final String createQuestionQuery = "INSERT INTO question (survey_id, question) VALUES (?,?)";
    private final String createChoiceQuery = "INSERT INTO choice (survey_id, question_id, choice_text) VALUES (?,?,?)";

    @Override
    public int createSurvey(Survey request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
//            KEEP FOR QUERY DEBUG
//            jdbcTemplate.update(createSurveyQuery, request.getHostUsername(), request.getTitle());

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(createSurveyQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, request.getHostUsername());
                ps.setString(2, request.getTitle());
                return ps;
            }, keyHolder);

        } catch (Exception e) {
            throw e;
        }

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public int createQuestion(Question request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(createQuestionQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, request.getSurveyId());
                ps.setString(2, request.getQuestion());
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            throw e;
        }

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public int createChoice(Choice request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(createChoiceQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, request.getSurveyId());
                ps.setInt(2, request.getQuestionId());
                ps.setString(3, request.getChoice());
                return ps;
            }, keyHolder);
        } catch (Exception e) {
            throw e;
        }

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }
}
