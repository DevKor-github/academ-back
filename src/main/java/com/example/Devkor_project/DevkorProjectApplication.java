package com.example.Devkor_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevkorProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevkorProjectApplication.class, args);
	}

}
