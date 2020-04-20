package com.metrics.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.metrics.service.ErrorHandler.HttpExceptionMessage;

import com.metrics.service.ErrorHandler.TypeError;

public class BusinessValidations {

	public static boolean ifSprintExist(String id) {
		boolean response = false;
		final String uri = "http://sprints-qa.us-east-2.elasticbeanstalk.com/sprints/" + id;
		RestTemplate restTemplate = new RestTemplate();
		try {
			if (!restTemplate.getForObject(uri, String.class).isEmpty())
				response = true;
		} catch (Exception error) {
			TypeError.httpErrorMessage(error, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
					HttpExceptionMessage.Sprint_idConflict409, "/metric/" + id);
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
		return response;
	}

	public static boolean ifUserExist(String id, int typeId) {
		boolean response = false;
		final String uri = "http://sourcescusersapi-test.us-west-1.elasticbeanstalk.com/api/users/" + id;
		RestTemplate restTemplate = new RestTemplate();
		try {
			if (!restTemplate.getForObject(uri, String.class).isEmpty())
				response = true;
		} catch (Exception e) {
			if (typeId == 1) {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
						HttpExceptionMessage.Evaluator_idConflict409, "/metric/" + id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			} else if (typeId == 0) {
				TypeError.httpErrorMessage(new Exception(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(),
						HttpExceptionMessage.Evaluated_idConflict409, "/metric/" + id);
				throw new ResponseStatusException(HttpStatus.CONFLICT);
			}

		}
		return response;
	}
}
