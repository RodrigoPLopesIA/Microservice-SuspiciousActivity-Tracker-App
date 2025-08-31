package com.rodrigo.ms.suspicious_activity_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SuspiciousActivityTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuspiciousActivityTrackerApplication.class, args);
	}

}
