package com.security.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.security.jwt.config.SecurityAuditorAware;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class JwtApplication {

	@Bean
    AuditorAware<String> auditorProvider() {
        return new SecurityAuditorAware();
    }

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

}
