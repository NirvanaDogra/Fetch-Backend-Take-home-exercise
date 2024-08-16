package com.nirvana.fetch_assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.nirvana.fetch_assessment.repository")
public class FetchAssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(FetchAssessmentApplication.class, args);
	}

}
