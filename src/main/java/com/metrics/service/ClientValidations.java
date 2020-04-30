package com.metrics.service;
import org.springframework.web.client.RestTemplate;

import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class ClientValidations {

	public static String sprintsApi(String id) {
		final String uri = StaticVariables.urlSprint + id;
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(uri, String.class);
	}

	public static String usersApi(String id) {
		final String uri = StaticVariables.urlUsers + id;
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(uri, String.class);
	}
}
