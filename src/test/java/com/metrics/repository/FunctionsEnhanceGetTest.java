package com.metrics.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.metrics.model.MetricsCollection;
import com.metrics.service.Functions;

public class FunctionsEnhanceGetTest {
	
	public void getMetricsEvaluated_id(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2000-03-20";
		String evaluated_id = "5e71620df59ec77b5164aaad";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}&evaluated_id={evaluated_id}";

		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate, evaluated_id);

		validatingExpectedResult(mvcResult);
	}

	public void getMetricsEvaluator_id(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2000-03-20";
		String evaluator_id = "5e71620df59ec77b5163aaad";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}&evaluator_id={evaluator_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate, evaluator_id);

		validatingExpectedResult(mvcResult);

	}

	public void getMetricsSprint_id(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2000-03-20";
		String sprint_id = "5e71620df59ec77b5165aaad";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}&spring_id={sprint_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate, sprint_id);

		validatingExpectedResult(mvcResult);
	}

	public void getMetricsRangeDate(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2000-03-20";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate);

		validatingExpectedResult(mvcResult);
	}

	public void geFailMetricsEvaluated_id(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2000-03-20";
		String evaluated_id = "9e71620df59ec77b5164aaad";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}&evaluated_id={evaluated_id}";

		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate, evaluated_id);

		validatingFailResult(mvcResult);
	}

	public void getFailMetricsEvaluator_id(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2000-03-20";
		String evaluator_id = "9e71620df59ec77b5163aaad";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}&evaluator_id={evaluator_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate, evaluator_id);

		validatingFailResult(mvcResult);

	}

	public void getFailMetricsSprint_id(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2000-03-20";
		String sprint_id = "9e71630df59ec77b5165aaad";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}&sprint_id={sprint_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate, sprint_id);

		validatingFailResult(mvcResult);
	}

	private MvcResult mvcEnhanceGetRequest(MockMvc mvc, String uri, String startDate, String endDate,
			String id) throws Exception {
		return mvc.perform(MockMvcRequestBuilders.get(uri, startDate, endDate, id)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	}

	private MvcResult mvcEnhanceGetRequest(MockMvc mvc, String uri, String startDate, String endDate) throws Exception {
		return mvc.perform(MockMvcRequestBuilders.get(uri, startDate, endDate).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}

	private void validatingExpectedResult(MvcResult mvcResult)
			throws JsonParseException, JsonMappingException, IOException {
		assertEquals(200, mvcResult.getResponse().getStatus());
		String content = mvcResult.getResponse().getContentAsString();
		MetricsCollection[] metricsCollection = Functions.mapFromJson(content, MetricsCollection[].class);
		System.out.println(metricsCollection.length);
		assertTrue(metricsCollection.length > 0);
	}

	private void validatingFailResult(MvcResult mvcResult)
			throws JsonParseException, JsonMappingException, IOException {
		assertEquals(404, mvcResult.getResponse().getStatus());
	}
}