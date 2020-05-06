package com.metrics.controller.ControllerFunctions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.metrics.service.StaticFunctionsVariables.StaticVariables;

public class FunctionsEnhanceGetTest {

	public void getMetricsEvaluated_id(MockMvc mvc) throws Exception {

		String uri = "/metrics?evaluated_id={evaluated_id}";

		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, StaticVariables.evaluated_idLocal);

		validatingExpectedResult(mvcResult);
	}

	public void getMetricsEvaluator_id(MockMvc mvc) throws Exception {


		String uri = "/metrics?evaluator_id={evaluator_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, StaticVariables.evaluator_idLocal);

		validatingExpectedResult(mvcResult);

	}
	public void getMetricsEvaluator_idSC(MockMvc mvc) throws Exception {


		String uri = "/metrics?evaluator_id={evaluator_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, "adas$#%sd");
		assertEquals(400, mvcResult.getResponse().getStatus());
	}

	public void getMetricsSprint_id(MockMvc mvc) throws Exception {

		String uri = "/metrics?spring_id={sprint_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, StaticVariables.sprint_idLocal);

		validatingExpectedResult(mvcResult);
	}

	public void getMetricsRangeDate(MockMvc mvc) throws Exception {

		String startDate = "2000-01-01";
		String endDate = "2021-03-20";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate);

		validatingExpectedResult(mvcResult);
	}

	public void geFailMetricsEvaluated_id(MockMvc mvc) throws Exception {

		String uri = "/metrics?evaluated_id={evaluated_id}";

		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, StaticVariables.evaluated_id+"1");

		validatingFailResult(mvcResult);
	}

	public void getFailMetricsEvaluator_id(MockMvc mvc) throws Exception {

		String uri = "/metrics?evaluator_id={evaluator_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, StaticVariables.evaluator_id+"1");

		validatingFailResult(mvcResult);

	}

	public void getFailMetricsSprint_id(MockMvc mvc) throws Exception {

		String uri = "/metrics?sprint_id={sprint_id}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, StaticVariables.sprint_id+"1");

		validatingFailResult(mvcResult);
	}
	public void getFailInvalidOrderByValue(MockMvc mvc) throws Exception {

		String uri = "/metrics?orderBy={orderByStr}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, -1);

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailPageHaveLetters(MockMvc mvc) throws Exception {

		String uri = "/metrics?page={pageStr}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, "asd");

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailInvalidSizeValue(MockMvc mvc) throws Exception {

		String uri = "/metrics?size={sizeStr}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, -1);

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailInvalidPageValue(MockMvc mvc) throws Exception {

		String uri = "/metrics?page={pageStr}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, -1);

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailSizeHaveLetters(MockMvc mvc) throws Exception {

		String uri = "/metrics?size={sizeStr}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, "1ds");

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailOrderByHaveLetters(MockMvc mvc) throws Exception {

		String uri = "/metrics?orderBy={orderByStr}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, "1ds");

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailInvalidValueOrderBy(MockMvc mvc) throws Exception {

		String uri = "/metrics?orderBy={sizeStr}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, 2);

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailDatesWrongDay(MockMvc mvc) throws Exception {

		String startDate = "2020-01-32";
		String endDate = "2021-03-20";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate);

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailDatesWrongMonth(MockMvc mvc) throws Exception {

		String startDate = "2020-14-01";
		String endDate = "2021-03-20";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate);

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailDatesWrongFormat(MockMvc mvc) throws Exception {

		String startDate = "2020-4-01";
		String endDate = "2021-03-20";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate);

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
	public void getFailDatesWrongOrder(MockMvc mvc) throws Exception {

		String startDate = "2021-05-01";
		String endDate = "2021-03-20";

		String uri = "/metrics?startDate={startDate}&endDate={endDate}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, startDate, endDate);

		assertEquals(400, mvcResult.getResponse().getStatus());
	}

	private MvcResult mvcEnhanceGetRequest(MockMvc mvc, String uri, String id)
			throws Exception {
		return mvc.perform(
				MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
	private MvcResult mvcEnhanceGetIdsRequest(MockMvc mvc, String uri, String id1, String id2)
			throws Exception {
		return mvc.perform(
				MockMvcRequestBuilders.get(uri, id1, id2).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
	private MvcResult mvcEnhanceGetIdsRequest(MockMvc mvc, String uri, String id1, String id2, String id3)
			throws Exception {
		return mvc.perform(
				MockMvcRequestBuilders.get(uri, id1, id2, id3).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
	private MvcResult mvcEnhanceGetRequest(MockMvc mvc, String uri, int value)
			throws Exception {
		return mvc.perform(
				MockMvcRequestBuilders.get(uri, value).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
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
		assertEquals(204, mvcResult.getResponse().getStatus());
	}

	public void getMetricsWithoutSprint_id(MockMvc mvc) throws Exception {
		String uri = "/metrics?evaluated_id={evaluated_id}&evaluator_id={evaluator_id}";

		MvcResult mvcResult = mvcEnhanceGetIdsRequest(mvc, uri, StaticVariables.evaluated_idLocal, StaticVariables.evaluator_idLocal);

		validatingExpectedResult(mvcResult);
		
	}

	public void getMetricsWithoutEvaluated_id(MockMvc mvc) throws Exception {
		String uri = "/metrics?sprint_id={sprint_id}&evaluator_id={evaluator_id}";

		MvcResult mvcResult = mvcEnhanceGetIdsRequest(mvc, uri, StaticVariables.sprint_idLocal, StaticVariables.evaluator_idLocal);

		validatingExpectedResult(mvcResult);
		
	}

	public void getMetricsWithoutEvaluator_id(MockMvc mvc) throws Exception {
		String uri = "/metrics?sprint_id={sprint_id}&evaluated_id={evaluated_id}";

		MvcResult mvcResult = mvcEnhanceGetIdsRequest(mvc, uri, StaticVariables.sprint_idLocal, StaticVariables.evaluated_idLocal);

		validatingExpectedResult(mvcResult);
		
	}

	public void getMetricsAllIds(MockMvc mvc) throws Exception {
		String uri = "/metrics?sprint_id={sprint_id}&evaluated_id={evaluated_id}&evaluator_id={evaluator_id}";

		MvcResult mvcResult = mvcEnhanceGetIdsRequest(mvc, uri, StaticVariables.sprint_idLocal, StaticVariables.evaluated_idLocal, StaticVariables.evaluator_idLocal);

		validatingExpectedResult(mvcResult);
		
	}

	public void getOrderByASC(MockMvc mvc) throws Exception {
		String uri = "/metrics?orderBy={orderByStr}";
		MvcResult mvcResult = mvcEnhanceGetRequest(mvc, uri, 0);

		assertEquals(200, mvcResult.getResponse().getStatus());
		
	}
}
