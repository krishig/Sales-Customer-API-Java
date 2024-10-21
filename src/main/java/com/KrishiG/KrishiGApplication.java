package com.KrishiG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.KrishiG.entities"})
@EnableJpaRepositories(basePackages = "com.KrishiG.repositories")
public class KrishiGApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrishiGApplication.class, args);
	}
}
