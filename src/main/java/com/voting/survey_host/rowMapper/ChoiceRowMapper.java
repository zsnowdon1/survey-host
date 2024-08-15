package com.voting.survey_host.rowMapper;

import com.voting.survey_host.entity.Choice;
import com.voting.survey_host.entity.Question;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChoiceRowMapper implements RowMapper<Choice> {
    private Long questionId;

    public ChoiceRowMapper(Long surveyId) {
        this.questionId = questionId;
    }

    @Override
    public Choice mapRow(ResultSet rs, int rowNum) throws SQLException {
        Choice choice = new Choice();
        choice.setQuestionId(rs.getLong("choice_id"));
        choice.setQuestionId(questionId);
        choice.setChoice(rs.getString("choice"));
        return choice;
    }
}
