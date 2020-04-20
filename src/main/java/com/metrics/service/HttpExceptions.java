package com.metrics.service;

public class HttpExceptions {

	public static final String findById404 = "The given ID does not match with any metric";
	public static final String getMetricsDate400 = "The given endDate is bigger than startDate";
	public static final String deleteMetric404 = "The given ID does not match with any metric";
	public static final String updateMetric404 = "The given ID does not match with any metric";
	public static final String getMetricsPaginationSize400 = "The given size is invalid";
	public static final String getMetricsPaginationPage400 = "The given page size is invalid";
	public static final String getMetricsPaginationRange400 = "The given page size is invalid";
	public static final String evaluatedId404 = "The given evaluated_id does not match with any user";
	public static final String evaluatorId404 = "The given evaluator_id does not match with any user";
	public static final String userId404 = "The given evaluator_id or evaluated_id do not match with any user";
	public static final String sprintId404 = "The given sprint_id does not match with any sprint";
	public static final String isDBEmpty204 = "There are not any records";
	public static final String VerifyingDateValid400 = "The given date is invalid";
	public static final String VerifyingUUID400 = "The given UUID has incorrect format";
	public static final String testMetricIntegrityId400 = "The id field must not exist";
	public static final String testMetricIntegrityIdUsers400 = "Evaluator and Evaluated ID must not be the same";
	public static final String testMetricIntegrityJson400 = "The given Json structure is not correct";
	public static final String testMetricIntegrityMetrics400 = "The given Metrics object are null or have invalid structure";
	public static final String testMetricIntegrityBlockers400 = "The gicen Blockers object are null or have invalid structure";
	public static final String testMetricIntegrityProactive400 = "The given Proactive object is null or have invalid structure";
	public static final String testMetricIntegrityRetroactive400 = "The given Retroactive object is null or have invalid structure";
	public static final String testMetricIntegrityType400 = "Type field should not be null";
	public static final String testMetricIntegrityEvaluatedId400 = "The given evaluated_id fiel is invalid or null";
	public static final String testMetricIntegrityEvaluatorId400 = "The given evaluator_id fiel is invalid or null";
	public static final String testMetricIntegritySprintId400 = "The given sprint_id fiel is invalid or null";
}
