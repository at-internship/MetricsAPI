package com.metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.metrics.service.MongoBasicServiceImpl;

@RestController
public class MetricsController {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MetricsController.class);
	
	@Autowired
	MongoBasicServiceImpl service;
	
	@ResponseStatus(value = HttpStatus.OK)
	@GetMapping("/greetings")
	public String greetings(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new String("Hello " + name + "!");
	}
	
}
