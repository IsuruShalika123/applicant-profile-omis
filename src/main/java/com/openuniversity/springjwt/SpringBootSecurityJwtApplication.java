package com.openuniversity.springjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.openuniversity.springjwt.models.AuditorAwareImpl;



@SpringBootApplication
@ComponentScan(basePackages = {"com.openuniversity.springjwt"})
@EntityScan(basePackages = {"com.openuniversity.springjwt.models"})
@EnableJpaRepositories(basePackages = {"com.openuniversity.springjwt.repository"})
@EnableAutoConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SpringBootSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}
	
	@Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

}
