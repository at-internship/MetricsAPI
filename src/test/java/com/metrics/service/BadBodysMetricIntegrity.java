package com.metrics.service;

import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.blockersString;
import com.metrics.model.metricsString;
import com.metrics.model.proactiveString;
import com.metrics.model.retroactiveString;
import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class BadBodysMetricIntegrity {
	
	public static final CreateMetricRequest createMetricTypeNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_id, null, "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricTypeEmptyRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_id, "", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricMetricsNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_id, "Empty", "2020-03-17",
			StaticVariables.sprint_idLocal, null);
	public static final CreateMetricRequest createMetricAttendanceNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString(null, "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricCarriedNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", null, new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricAttendanceInvalidValueRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("hello", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricCarriedInvalidValueRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("true", "falses", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricBlockersNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", null,
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricBlockedNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString(null, "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricBlockedInvalidValueRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("falsess", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricBlockersCommentNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", null),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricProactiveNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					null, new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricLookedNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString(null, "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricLookedEmptyRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricProvidedNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", null, "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricProvidedEmptyRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricWorkedNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", null, "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricWorkedEmptyRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricSharedNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", null), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricSharedEmptyRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", ""), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricRetroactiveNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), null));
	public static final CreateMetricRequest createMetricDelayedNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString(null, "Empty")));
	public static final CreateMetricRequest createMetricDelayedInvalidValueRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("falsesss", "Empty")));
	public static final CreateMetricRequest createMetricRetroactiveCommentNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_idLocal, StaticVariables.evaluated_idLocal, "Empty", "2020-03-17", StaticVariables.sprint_idLocal,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", null)));
	public static final CreateMetricRequest createMetricDateNullPostRequest = new CreateMetricRequest(
			StaticVariables.evaluator_id, StaticVariables.evaluated_id, "Empty", null, StaticVariables.sprint_id,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricDateNullPutRequest = new CreateMetricRequest(
			StaticVariables.evaluator_id, StaticVariables.evaluated_id, "Empty", null, StaticVariables.sprint_id,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricDateEmptyRequest = new CreateMetricRequest(
			StaticVariables.evaluator_id, StaticVariables.evaluated_id, "Empty", "", StaticVariables.sprint_id,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricEvaluatorNullRequest = new CreateMetricRequest(
			null, StaticVariables.evaluated_id, "Empty", "2020-03-17", StaticVariables.sprint_id,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricEvaluatedNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_id, null, "Empty", "2020-03-17", StaticVariables.sprint_id,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	public static final CreateMetricRequest createMetricSprintNullRequest = new CreateMetricRequest(
			StaticVariables.evaluator_id, StaticVariables.evaluated_id, "Empty", "2020-03-17", null,
			new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
					new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
}
