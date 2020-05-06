package com.metrics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.web.server.ResponseStatusException;

import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class VerifyingSprint {
	
	public static void ifSprintExistTest() {
		assertEquals(true, BusinessMethods.ifSprintExist(StaticVariables.sprint_id));
	}
	
	
	public static void ifSprintNotExistTest() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.ifSprintExist(StaticVariables.wrongSprint_idLocal));
	}
}
