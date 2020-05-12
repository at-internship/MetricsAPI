package com.metrics.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import com.metrics.service.StaticFunctionsVariables.StaticVariables;

class TechnicalValidationsTest {

	@Test
	void verifyingIfNegativeNumberNotNumberTest() {
		assertEquals(0, TechnicalValidations.verifyingIfNegativeNumber("-a"));
	}
	
	@Test
	void VerifyingIDTest() {
		assertEquals(false, TechnicalValidations.VerifyingID("sadasdasda"));
	}
	
	@Test
	void VerifyingUUID_EvaluatorTest() {
		assertThrows(ResponseStatusException.class,() -> TechnicalValidations.VerifyingUUID_Evaluator("sadasdasda", StaticVariables.id));
	}
	
	@Test
	void VerifyingUUID_EvaluatedTest() {
		assertThrows(ResponseStatusException.class,() -> TechnicalValidations.VerifyingUUID_Evaluated("sadasdasda", StaticVariables.id));
	}
	
	@Test
	void VerifyingUUID_SprintTest() {
		assertThrows(ResponseStatusException.class,() -> TechnicalValidations.VerifyingUUID_Sprint("sadasdasda", StaticVariables.id));
	}
	
	@Test
	public void VerifyingClassTechnicalValidations() {
		TechnicalValidations technicalValidations = new TechnicalValidations();
		assertNotNull(technicalValidations);
	}
	
}
