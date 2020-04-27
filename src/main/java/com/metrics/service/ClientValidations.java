package com.metrics.service;
import org.springframework.web.client.RestTemplate;

public class ClientValidations {

	public static String sprintsApi(String id) {
		final String uri = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/" + id;
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(uri, String.class);
	}

	public static String usersApi(String id) {
		final String uri = "http://sourcescusersapi-test.us-west-1.elasticbeanstalk.com/api/users/" + id;
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(uri, String.class);
	}
}
