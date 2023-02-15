package com.sparta.springhomework2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringHomework2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringHomework2Application.class, args);
	}

}
