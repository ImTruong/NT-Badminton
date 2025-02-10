package com.dev.NT_Badminton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NtBadmintonApplication {

	public static void main(String[] args) {
		SpringApplication.run(NtBadmintonApplication.class, args);
	}

}
