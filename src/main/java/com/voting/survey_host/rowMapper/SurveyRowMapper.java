package com.voting.survey_host.rowMapper;

import com.voting.survey_host.entity.Survey;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SurveyRowMapper implements RowMapper<Survey> {

    private String hostname;

    public SurveyRowMapper(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public Survey mapRow(ResultSet rs, int rowNum) throws SQLException {
        Survey survey = new Survey();
        survey.setSurveyId(rs.getLong("survey_id"));
        survey.setHostUsername(hostname);
        survey.setTitle(rs.getString("title"));
        return survey;
    }
}
