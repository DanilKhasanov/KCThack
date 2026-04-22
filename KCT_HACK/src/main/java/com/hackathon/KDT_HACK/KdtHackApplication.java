package com.hackathon.KDT_HACK;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableJpaRepositories(basePackages = "com.hackathon.KDT_HACK.repository")
@EntityScan(basePackages = "com.hackathon.KDT_HACK.entity")
public class KdtHackApplication {

	public static void main(String[] args) {
		SpringApplication.run(KdtHackApplication.class, args);
	}

}
