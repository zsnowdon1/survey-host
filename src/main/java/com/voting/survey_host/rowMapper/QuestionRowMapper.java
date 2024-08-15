package com.voting.survey_host.rowMapper;

import com.voting.survey_host.entity.Question;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class QuestionRowMapper implements RowMapper<Question> {

    private Long surveyId;

    public QuestionRowMapper(Long surveyId) {
        this.surveyId = surveyId;
    }

    @Override
    public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
        Question question = new Question();
        question.setQuestionId(rs.getLong("question_id"));
        question.setSurveyId(surveyId);
        question.setQuestion(rs.getString("question"));
        question.setChoices(new ArrayList<>());
        return question;
    }
}
