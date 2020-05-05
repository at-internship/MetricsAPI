package com.metrics.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.server.ResponseStatusException;

import com.metrics.service.StaticFunctionsVariables.StaticVariables;


public class VerifyingMetricIntegrity {

	public static void VerifyingTypeNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricTypeNullRequest, 0));
		
	}
	public static void VerifyingTypeEmpty() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricTypeEmptyRequest, 0));
		
	}
	
	public static void VerifyingMetricsNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricMetricsNullRequest, 0));
		
	}
	public static void VerifyingMetricsAttendanceNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricAttendanceNullRequest, 0));
		
	}
	public static void VerifyingMetricsAttendanceInvalidValue() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricAttendanceInvalidValueRequest, 0));
		
	}
	public static void VerifyingMetricsCarriedNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricCarriedNullRequest, 0));
		
	}
	public static void VerifyingMetricsCarriedInvalidValue() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricCarriedInvalidValueRequest, 0));
		
	}
	public static void VerifyingMetricsBlockersNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricBlockersNullRequest, 0));
		
	}
	public static void VerifyingMetricsBlockersBlockedNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricBlockedNullRequest, 0));
		
	}
	public static void VerifyingMetricsBlockersBlockedInvalidValue() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricBlockedInvalidValueRequest, 0));
		
	}
	public static void VerifyingMetricsBlockersCommentNull() {
		assertEquals("", BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricBlockersCommentNullRequest, 0).getMetrics().getBlockers().getComments());
		
	}
	
	public static void VerifyingMetricsProactiveNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricProactiveNullRequest, 0));
		
	}
	public static void VerifyingMetricsLookedNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricLookedNullRequest, 0));
		
	}
	public static void VerifyingMetricsLookedEmpty() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricLookedEmptyRequest, 0));
		
	}
	public static void VerifyingMetricsProvidedNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricProvidedNullRequest, 0));
		
	}
	public static void VerifyingMetricsProvidedEmpty() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricProvidedEmptyRequest, 0));
		
	}
	public static void VerifyingMetricsWorkedNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricWorkedNullRequest, 0));
		
	}
	public static void VerifyingMetricsWorkedEmpty() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricWorkedEmptyRequest, 0));
		
	}
	public static void VerifyingMetricsSharedNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricSharedNullRequest, 0));
		
	}
	public static void VerifyingMetricsSharedEmpty() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricSharedEmptyRequest, 0));
		
	}
	public static void VerifyingMetricsRetroactiveNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricRetroactiveNullRequest, 0));
		
	}
	public static void VerifyingMetricsDelayedNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricDelayedNullRequest, 0));
		
	}
	public static void VerifyingMetricsDelayedInvalidValue() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricDelayedInvalidValueRequest, 0));
		
	}
	public static void VerifyingMetricsRetroactiveCommentNull() {
		assertEquals("", BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricRetroactiveCommentNullRequest, 0).getMetrics().getRetroactive().getComments());
		
	}
	public static void VerifyingMetricsDateNullPost() {
		
		assertEquals(dateFormat.format(date).toString(), BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricDateNullPostRequest, 0).getDate());
		
	}
	public static void VerifyingMetricsDateNullPut() {
		assertEquals(StaticVariables.datePUT.getDate(), BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricDateNullPutRequest, 1).getDate());
		
	}
	public static void VerifyingMetricsDateEmptyPost() {
		
		assertEquals(dateFormat.format(date).toString(), BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricDateEmptyRequest, 0).getDate());
		
	}
	public static void VerifyingMetricsDateEmptyPut() {
		
		assertEquals(dateFormat.format(date).toString(), BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricDateEmptyRequest, 1).getDate());
		
	}
	public static void VerifyingMetricsEvaluatorNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricEvaluatorNullRequest, 0));
		
	}
	public static void VerifyingMetricsEvaluatedNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricEvaluatedNullRequest, 0));
		
	}
	public static void VerifyingMetricsSprintNull() {
		assertThrows(ResponseStatusException.class,() -> BusinessMethods.testMetricIntegrity(BadBodysMetricIntegrity.createMetricSprintNullRequest, 0));
		
	}
	static Date date = new Date();
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
}
