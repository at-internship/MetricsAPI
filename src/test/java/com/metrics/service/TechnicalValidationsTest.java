package com.metrics.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
	public void VerifyingClassTechnicalValidations() {
		TechnicalValidations technicalValidations = new TechnicalValidations();
		assertNotNull(technicalValidations);
	}
}
