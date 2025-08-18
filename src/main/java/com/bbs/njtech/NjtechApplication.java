package com.bbs.njtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class NjtechApplication {

	public static void main(String[] args) {
		SpringApplication.run(NjtechApplication.class, args);
	}

}
