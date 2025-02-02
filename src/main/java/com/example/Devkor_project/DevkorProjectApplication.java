package com.example.Devkor_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling	// SchedulerService를 위한 Annotation
public class DevkorProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevkorProjectApplication.class, args);
	}

}
