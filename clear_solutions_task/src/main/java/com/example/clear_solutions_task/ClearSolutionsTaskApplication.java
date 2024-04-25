package com.example.clear_solutions_task;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@OpenAPIDefinition
@EnableJpaAuditing
public class ClearSolutionsTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClearSolutionsTaskApplication.class, args);
	}

}
