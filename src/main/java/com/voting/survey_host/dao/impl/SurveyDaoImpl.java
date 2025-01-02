package com.voting.survey_host.dao.impl;

import com.voting.survey_host.dao.SurveyDao;
import com.voting.survey_host.dao.SurveyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import static com.voting.survey_host.entity.DBConstants.*;

import java.sql.PreparedStatement;
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

}
