package com.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class MetricsApplication {
	public static final Logger logger = LogManager.getLogger(MetricsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);
		
	}
}