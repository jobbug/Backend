package com.example.jobbug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JobbugApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobbugApplication.class, args);
	}

}
