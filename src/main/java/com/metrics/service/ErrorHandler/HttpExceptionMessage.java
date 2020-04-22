package com.metrics.service.ErrorHandler;

import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class HttpExceptionMessage {
	//Messages of class MetricControllers
	//Massage of method "getMetrics"
	public static final String DateInvalidOrder400 = "The given value for startDate is higger than endDate";
	public static final String InvalidPageOrSizeValue400 = "The given value for startDate is higger than endDate";
	public static final String OrderByInvalidValue400 = "The given value for orderBy is invalid use '1'(to use sort descending) or '0'(to use sort ascending)";
	
	
	//Message of class MetricServiceImpl
	
	//Message of method "findById", "deleteMetric" and "updateMetric"
	public static final String IdNotFound404 = "Has not been found a metric with the Id that has given";
	//Message of method "getAllMetricsPaginated"
	public static final String PaginationInvalidRange400 = "Has been found problems with range of metric list";
	//Messages for method "getItemsFromIdFilter"
	public static final String Evaluated_IdNotFound404 = "Has not been found a metric with the evaluated_id that has given";
	public static final String Evaluator_IdNotFound404 = "Has not been found a metric with the evaluator_id that has given";
	public static final String Sprint_IdNotFound404 = "Has not been found a metric with the sprint_id that has given";
	
	//Messages of class Function
	
	//Message for method "IsDBEmpty"
	public static final String DBIsEmpty204 = "The DB has not any records";
	//Messages for method "VerifyingDateValid" and "stringToDate"
	public static final String DateInvalidDay400 = "The given date has an invalid day";
	public static final String DateInvalidFormat400 = "The given date has incorrect format";
	public static final String DateInvalidMonth400 = "The given date has an invalid Month";
	public static final String DateInvalidRange400 = "The given date is out of range";
	public static final String DateYearIsLeap400 = "you can't use that day, the Month has 29 days";
	public static final String DateYearIsNotLeap400 = "you can't use that day, the Month has 28 days";
	//Message for method "VerifyingUUID" and "VerifyingID"
	public static final String IDInvalid400 = "The given ID has incorrect format";
	//Messages for method "testMetricIntegrity"
	public static final String FieldAttendanceNull400 = "The field attendance is required";
	public static final String FieldBlockedNull400 = "The field blockers is required";
	public static final String FieldCarried_OverNull400 = "The field carried_over is required";
	public static final String FieldDelayed_looking_helpNull400 = "The field delayed_looking_help_over is required";
	public static final String FieldEvaluated_idNull400 = "The field evaluated_id is required";
	public static final String FieldEvaluator_idNull400 = "The field evaluator_id is required";
	public static final String FieldLooked_for_help400 = "The field looked_for_help is required";
	public static final String FieldProvided_help400 = "The field provided_help is required";
	public static final String FieldShared_resources400 = "The field shared_resources is required";
	public static final String FieldSprint_id400 = "The field sprint_id is required";
	public static final String FieldTypeNull400 = "The field type that is required";
	public static final String FieldWorked_ahead400 = "The field worked_ahead is required";
	public static final String IdHasSpecialChar400 = "The given ID has invalid characters";
	public static final String IdIsInBody400 = "The request body must not has id";
	public static final String JsonInvalidFormat400 = "Json structure is not correct";
	public static final String SameIDs400 = "The evaluated_id and evaluator_id that have given must not be equals";
	public static final String ObjectBlockersNull400 = "The object blockers is required";
	public static final String ObjectMetricNull400 = "The object metric is required";
	public static final String ObjectProactiveNull400 = "The object proactive is required";
	public static final String ObjectRetroactiveNull400 = "The object retroactive is required";
	
	//Message for method "ifSprintExist"
	public static final String Sprint_idConflict409 = "The sprint_id that has given does not exist";
	
	//Messages for method "ifUserExist"
	public static final String Evaluated_idConflict409 = "The evaluated_id that has given does not exist";
	public static final String Evaluator_idConflict409 = "The evaluator_id that has given does not exist";
	
	//Messages for method "checkPaginationParams"
	public static final String PageNull400 = "page value field is required when size is given";
	public static final String SizeNull400 = "size value field is required when page is given";
	public static final String isOnlyNumberFail400 = "OrderBy, Size and Page must be numbers";
	
	//Messages for method "checkDateParams"
	public static final String EndDateNull400 = "endDate value field is required when startDate is given";
	public static final String StartDateNull400 = "startDate value field is required when endDate is given";
	
	
	//Messages for method "checkParams"
	public static final String InvalidParameter400 = "An invalid request param  called " + StaticVariables.parameterName + " has been entered";
	public static final String ParameterNull400 = "The id key  " + StaticVariables.parameterName + " can not be null or empty";
}
