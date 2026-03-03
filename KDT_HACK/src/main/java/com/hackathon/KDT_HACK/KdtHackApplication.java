package com.hackathon.KDT_HACK;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class KdtHackApplication {

	public static void main(String[] args) {
		SpringApplication.run(KdtHackApplication.class, args);
	}

}
