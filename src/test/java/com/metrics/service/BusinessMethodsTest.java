package com.metrics.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class BusinessMethodsTest {
	
	//assertThrows(ResponseStatusException.class,() -> BusinessMethods.ifSprintExist(StaticVariables.sprint_id+1));
	@Test
	public void VerifyingSprintTest() {
		VerifyingSprint.ifSprintExistTest();
		VerifyingSprint.ifSprintNotExistTest();
	}
	
	
	//0 = evaluated_id
	//1 = evaluator_id
	@Test
	public void VerifyingUserTest() {
		VerifyingUser.ifUserExistTest();
		VerifyingUser.ifUserNotExistTest();
	}
	
	//0 = POST request
	//1 = PUT request
	//2 = GET request
	@Test
	public void VerifyingDateTest() {
		VerifyingDate.VerifyingDateValidTest();
		VerifyingDate.VerifyingDateOnlyNumbersTest();
		VerifyingDate.VerifyingDateOnlyLettersTest();
		VerifyingDate.VerifyingDateIncompletElementsTest();
		VerifyingDate.VerifyingDateManytElementsTest();
		VerifyingDate.VerifyingDatePutOrPostTest();
		VerifyingDate.VerifyingDateBadOrderTest();
		VerifyingDate.VerifyingDateBadYearTest();
		VerifyingDate.VerifyingDateBadYearWrongOrderTest();
		VerifyingDate.VerifyingDateBadDayTest();
		VerifyingDate.VerifyingDateBadMonthTest();
		VerifyingDate.VerifyingDateExceptionParseTest();
		VerifyingDate.VerifyingDateOutRangeTest();
		VerifyingDate.VerifyingDateOutRangeDayYearLeapTest();
		VerifyingDate.VerifyingDateOutRangeDayYearNotLeapTest();
		VerifyingDate.VerifyingDateInvalidDayTest();
	}
	
	@Test
	public void VerifyingMetricIntegrityTest() {
		
		VerifyingMetricIntegrity.VerifyingTypeNull();
		VerifyingMetricIntegrity.VerifyingTypeEmpty();
		VerifyingMetricIntegrity.VerifyingMetricsNull();
		VerifyingMetricIntegrity.VerifyingMetricsAttendanceNull();
		VerifyingMetricIntegrity.VerifyingMetricsAttendanceInvalidValue();
		VerifyingMetricIntegrity.VerifyingMetricsCarriedNull();
		VerifyingMetricIntegrity.VerifyingMetricsCarriedInvalidValue();
		VerifyingMetricIntegrity.VerifyingMetricsBlockersNull();
		VerifyingMetricIntegrity.VerifyingMetricsBlockersBlockedNull();
		VerifyingMetricIntegrity.VerifyingMetricsBlockersBlockedInvalidValue();
		VerifyingMetricIntegrity.VerifyingMetricsBlockersCommentNull();
		VerifyingMetricIntegrity.VerifyingMetricsProactiveNull();
		VerifyingMetricIntegrity.VerifyingMetricsLookedNull();
		VerifyingMetricIntegrity.VerifyingMetricsLookedEmpty();
		VerifyingMetricIntegrity.VerifyingMetricsProvidedNull();
		VerifyingMetricIntegrity.VerifyingMetricsProvidedEmpty();
		VerifyingMetricIntegrity.VerifyingMetricsWorkedNull();
		VerifyingMetricIntegrity.VerifyingMetricsWorkedEmpty();
		VerifyingMetricIntegrity.VerifyingMetricsSharedNull();
		VerifyingMetricIntegrity.VerifyingMetricsSharedEmpty();
		VerifyingMetricIntegrity.VerifyingMetricsRetroactiveNull();
		VerifyingMetricIntegrity.VerifyingMetricsDelayedNull();
		VerifyingMetricIntegrity.VerifyingMetricsDelayedInvalidValue();
		VerifyingMetricIntegrity.VerifyingMetricsRetroactiveCommentNull();
		VerifyingMetricIntegrity.VerifyingMetricsDateNullPost();
		VerifyingMetricIntegrity.VerifyingMetricsDateEmptyPost();
		VerifyingMetricIntegrity.VerifyingMetricsDateNullPut();
		VerifyingMetricIntegrity.VerifyingMetricsDateEmptyPut();
		VerifyingMetricIntegrity.VerifyingMetricsEvaluatorNull();
		VerifyingMetricIntegrity.VerifyingMetricsEvaluatedNull();
		VerifyingMetricIntegrity.VerifyingMetricsSprintNull();
	}
	
	@Test
	public void VerifyingClassBussiness() {
		BusinessMethods businessMethods = new BusinessMethods();
		assertNotNull(businessMethods);
	}
}
