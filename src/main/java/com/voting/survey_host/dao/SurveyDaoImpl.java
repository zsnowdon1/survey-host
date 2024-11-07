package com.voting.survey_host.dao;

import com.voting.survey_host.dto.AddQuestionRequest;
import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.ChoiceMapping;
import com.voting.survey_host.entity.Question;
import com.voting.survey_host.entity.Survey;
import com.voting.survey_host.resultSetExtractor.SurveyResultSetExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import static com.voting.survey_host.entity.DBConstants.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class SurveyDaoImpl implements SurveyDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SurveyDaoImpl.class);

    public SurveyDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long createEmptySurvey(String title) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createSurveyQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, "zsnowdon");
            ps.setString(2, title);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public long addQuestion(AddQuestionRequest question) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createQuestionQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, question.getSurveyId());
            ps.setString(2, question.getTitle());
            return ps;
        }, keyHolder);

        long questionId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        jdbcTemplate.batchUpdate(createChoiceQuery, question.getChoices(), question.getChoices().size(),
                (ps, choice) -> {
                    ps.setLong(1, questionId);
                    ps.setString(2, choice);
                });

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
    public int deleteSurvey(long surveyId) {
        int deleteCount = jdbcTemplate.update(deleteChoicesBySurvey, surveyId);
        logger.info("Deleted {} choices for survey {}", deleteCount, surveyId);
        int questionsDeleted = jdbcTemplate.update(deleteQuestionBySurvey, surveyId);
        logger.info("Deleted {} questions for survey {}", deleteCount, surveyId);
        return jdbcTemplate.update(deleteSurveyQuery, surveyId);
    }

    @Override
    public int deleteChoice(long choiceId) {
        return jdbcTemplate.update(deleteChoiceQuery, choiceId);
    }

    @Override
    public int deleteQuestion(long questionId) {
        int choicesDeleted = jdbcTemplate.update(deleteChoiceByQuestionQuery, questionId);
        logger.info("Deleted {} choices for question {}", choicesDeleted, questionId);
        return jdbcTemplate.update(deleteQuestionQuery, questionId);
    }

    @Override
    public List<ChoiceMapping> getChoiceMappings(long surveyId) {
        return jdbcTemplate.query(getChoicesBySurvey, new Object[]{surveyId}, rs -> {
            List<ChoiceMapping> choiceMappings = new ArrayList<>();
            while(rs.next()) {
                choiceMappings.add(new ChoiceMapping(rs.getLong("choice_id"), rs.getString("choice")));
            }
            return choiceMappings;
        });
    }
}
