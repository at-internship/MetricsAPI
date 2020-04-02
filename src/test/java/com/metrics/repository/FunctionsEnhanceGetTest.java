package com.metrics.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
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
		String evaluated_id = "5e6bbc854244ac0cbc8df65d";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}&evaluated_id={evaluated_id}";

		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate, evaluated_id);

		validatingExpectedResult(mvcResult);
	}

	public void getMetricsEvaluator_id(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2000-03-20";
		String evaluator_id = "5e6bbc854244ac0cbc8df65d";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}&evaluator_id={evaluator_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate, evaluator_id);

		validatingExpectedResult(mvcResult);

	}

	public void getMetricsSprint_id(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2000-03-20";
		String sprint_id = "5e78f5e792675632e42d1a69";

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
		String sprint_id = "5e78f5e792675632e42d1a70";

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
	}

	private void validatingFailResult(MvcResult mvcResult)
			throws JsonParseException, JsonMappingException, IOException {
		assertEquals(404, mvcResult.getResponse().getStatus());
	}
}
