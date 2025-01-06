package com.voting.survey_host;

import com.voting.survey_host.dao.impl.SurveyRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SurveyHostApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveyHostApplication.class, args);
	}

}
