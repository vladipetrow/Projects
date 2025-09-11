package com.example.workproject1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WorkProject1Application {
	public static void main(String[] args) {
		SpringApplication.run(WorkProject1Application.class, args);
	}

}
