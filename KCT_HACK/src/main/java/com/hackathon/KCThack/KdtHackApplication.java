package com.hackathon.KCThack;

import com.hackathon.KCThack.Capcha.RecaptchaProperties;
import com.hackathon.KCThack.TeamManagement.config.TeamProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableConfigurationProperties({TeamProperties.class, RecaptchaProperties.class})
public class KdtHackApplication {

	public static void main(String[] args) {
		SpringApplication.run(KdtHackApplication.class, args);
	}

}
