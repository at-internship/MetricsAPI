
package com.metrics.service.ErrorHandler;

import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class HttpExceptionMessage {
	//Messages of class MetricControllers
	//Massage of method "getMetrics"
	public static final String DateInvalidOrder400 = "StartDate is higger than endDate";
	public static final String InvalidPageAndSizeValue400 = "Page and size are out of range";
	public static final String InvalidPageValue400 = "Page had an invalid value";
	public static final String InvalidSizeValue400 = "Size had an invalid value";
	public static final String OrderByInvalidValue400 = "Value for orderBy is invalid use '1'(to use sort descending) or '0'(to use sort ascending)";
	
	
	//Message of class MetricServiceImpl
	
	//Message of method "findById", "deleteMetric" and "updateMetric"
	public static final String IdNotFound404 = "Metric Id not found";
	//Message of method "getAllMetricsPaginated"
	public static final String PaginationInvalidRange400 = "Has been found problems with range of metric list";
	//Messages for method "getItemsFromIdFilter"
	public static final String Evaluated_IdNotFound404 = "Evaluated not exist";
	public static final String Evaluator_IdNotFound404 = "Evaluator not exist";
	public static final String Sprint_IdNotFound404 = "Sprint id not exist";
	
	//Messages of class Function
	
	//Message for method "IsDBEmpty"
	public static final String DBIsEmpty204 = "DB has not any records";
	//Messages for method "VerifyingDateValid" and "stringToDate"
	public static final String DateInvalidDay400 = "Date has an invalid day";
	public static final String DateInvalidDayFormat400 = "Day format should be dd";
	public static final String DateInvalidFormat400 = "Date has incorrect format";
	public static final String DateInvalidMonth400 = "Date has an invalid Month";
	public static final String DateInvalidMonthFormat400 = "Month format should be MM";
	public static final String DateInvalidRange400 = "Date is out of range";
	public static final String DateInvalidYearFormat400 = "Year format should be yyyy";
	public static final String DateYearIsLeap400 = "Month has 29 days";
	public static final String DateYearIsNotLeap400 = "Month has 28 days";
	//Message for method "VerifyingUUID" and "VerifyingID"
	public static final String IDInvalid400 = "Metric Id has incorrect format";
	public static final String EvaluatorIDInvalid400 = "evaluator_Id incorrect format";
	public static final String EvaluatedIDInvalid400 = "evaluated_Id incorrect format";
	public static final String SprintIDInvalid400 = "sprint_id incorrect format";
	//Messages for method "testMetricIntegrity"
	public static final String isOnlyNumberPageFail400 = "Page must only have numbers";
	public static final String isOnlyNumberSizeFail400 = "Size must only have numbers";
	public static final String isOnlyNumberOrderByFail400 = "OrderBy must only have numbers";
	public static final String FieldAttendanceInvalid400 = "Attendance only admits 'true' or 'false'";
	public static final String FieldAttendanceNull400 = "Attendance is required";
	public static final String FieldBlockedInvalid400 = "Blocked only admits 'true' or 'false'";
	public static final String FieldBlockedNull400 = "Blocked is required";
	public static final String FieldCarried_OverInvalid400 = "Carried_over only admits 'true' or 'false'";
	public static final String FieldCarried_OverNull400 = "Carried_over is required";
	public static final String FieldDelayed_looking_helpInvalid400 = "Delayed_looking_help_over only admits 'true' or 'false'";
	public static final String FieldDelayed_looking_helpNull400 = "Delayed_looking_help_over is required";
	public static final String FieldEvaluated_idNull400 = "Evaluated_id is required";
	public static final String FieldEvaluator_idNull400 = "Evaluator_id is required";
	public static final String FieldLooked_for_helpInvalid400 = "Looked_for_help only admits 'true' or 'false'";
	public static final String FieldLooked_for_helpNull400 = "Looked_for_help is required";
	public static final String FieldProvided_helpInvalid400 = "Provided_help only admits 'true' or 'false'";
	public static final String FieldProvided_helpNull400 = "Provided_help is required";
	public static final String FieldShared_resourcesInvalid400 = "Shared_resources only admits 'true' or 'false'";
	public static final String FieldShared_resourcesNull400 = "Shared_resources is required";
	public static final String FieldSprint_id400 = "Sprint_id is required";
	public static final String FieldTypeNull400 = "Type that is required";
	public static final String FieldWorked_aheadInvalid400 = "Field worked_ahead only admits 'true' or 'false'";
	public static final String FieldWorked_aheadNull400 = "Field worked_ahead is required";
	public static final String IdHasSpecialChar400 = "Id has invalid characters";
	public static final String IdIsInBody400 = "Request body must not has id";
	public static final String JsonInvalidFormat400 = "Json structure is not correct";
	public static final String SameIDs400 = "Evaluated_id and evaluator_id that have given must not be equals";
	public static final String ObjectBlockersNull400 = "Object blockers is required";
	public static final String ObjectMetricNull400 = "Object metric is required";
	public static final String ObjectProactiveNull400 = "Object proactive is required";
	public static final String ObjectRetroactiveNull400 = "Object retroactive is required";
	public static final String MetricIdNull405 = "metric_id not found";
	
	//Message for method "ifSprintExist"
	public static final String Sprint_idConflict409 = "sprint_id not found";
	
	//Messages for method "ifUserExist"
	public static final String Evaluated_idConflict409 = "evaluated_id not exist";
	public static final String Evaluator_idConflict409 = "evaluator_id not exist";
	
	//Messages for method "checkPaginationParams"
	public static final String PageNull400 = "page value field is required when size is given";
	public static final String SizeNull400 = "size value field is required when page is given";
	
	//Messages for method "checkDateParams"
	public static final String EndDateNull400 = "endDate value field is required when startDate is given";
	public static final String StartDateNull400 = "startDate value field is required when endDate is given";
	
	
	//Messages for method "checkParams"
	public static final String InvalidParameter400 = "An invalid request param  called " + StaticVariables.parameterName + " has been entered";
	public static final String ParameterNull400 = "The id key  " + StaticVariables.parameterName + " can not be null or empty";
}