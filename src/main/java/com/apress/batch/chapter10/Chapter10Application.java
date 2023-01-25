package com.apress.batch.chapter10;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Chapter10Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter10Application.class, args);
	}

}
