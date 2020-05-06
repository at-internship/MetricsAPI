package com.metrics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.web.server.ResponseStatusException;

import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class VerifyingUser {
	
	public static void ifUserExistTest() {
		assertEquals(true, BusinessMethods.ifUserExist(StaticVariables.evaluator_id, 1));
	}
	
	
	public static void ifUserNotExistTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.ifUserExist(StaticVariables.wrongEvaluator_idLocal, 1));
	}
}
