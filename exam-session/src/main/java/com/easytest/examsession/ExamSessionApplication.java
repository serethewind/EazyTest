package com.easytest.examsession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ExamSessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamSessionApplication.class, args);
	}

}