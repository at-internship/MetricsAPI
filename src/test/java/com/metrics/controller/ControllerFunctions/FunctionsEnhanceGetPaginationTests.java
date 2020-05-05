package com.metrics.controller.ControllerFunctions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class FunctionsEnhanceGetPaginationTests {
	public void getMetricsPagination(MockMvc mvc) throws Exception {
		int sizeStr = 1;
		int pageStr = 1;
		String uri = "/metrics?page={pageStr}&size={sizeStr}";
		MvcResult mvcResult = mvcGetPaginationRequest(mvc, uri, pageStr, sizeStr);

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	public void getMetricsPaginationFailSize(MockMvc mvc) throws Exception {

		int sizeStr = 0;
		int pageStr = 0;
		String uri = "/metrics?page={pageStr}&size={sizeStr}";
		MvcResult mvcResult = mvcGetPaginationRequest(mvc, uri, pageStr, sizeStr);

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}
	public void getMetricsPaginationFailType(MockMvc mvc) throws Exception {

		int sizeStr = 0;
		String pageStr = "0";
		String uri = "/metrics?page={pageStr}&size={sizeStr}";
		MvcResult mvcResult = mvcGetPaginationRequest(mvc, uri, pageStr, sizeStr);

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}
	public void getMetricsPaginationOnlyPage(MockMvc mvc) throws Exception {
		int pageStr = 1;
		String uri = "/metrics?page={pageStr}";
		MvcResult mvcResult = mvcGetPaginationRequest(mvc, uri, pageStr);

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	public void getMetricsPaginationOnlySize(MockMvc mvc) throws Exception {
		int sizeStr = 1;
		String uri = "/metrics?size={sizeStr}";
		MvcResult mvcResult = mvcGetPaginationRequest(mvc, uri, sizeStr);

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	public void getMetricsEvaluator_idHaveOnlyNumbers(MockMvc mvc) throws Exception {
		String uri = "/metrics?evaluator_id={evaluator_id}";
		MvcResult mvcResult = mvcGetTypeId(mvc, uri, "12345432");
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
	}
	private MvcResult mvcGetPaginationRequest(MockMvc mvc, String uri, int start, int size) throws Exception {
		return mvc.perform(MockMvcRequestBuilders.get(uri, start, size).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
	private MvcResult mvcGetPaginationRequest(MockMvc mvc, String uri, String start, int size) throws Exception {
		return mvc.perform(MockMvcRequestBuilders.get(uri, start, size).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
	private MvcResult mvcGetPaginationRequest(MockMvc mvc, String uri, int valuePagination) throws Exception {
		return mvc.perform(MockMvcRequestBuilders.get(uri, valuePagination).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
	private MvcResult mvcGetTypeId(MockMvc mvc, String uri, String id) throws Exception {
		return mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}

	public void getMetricsEvaluated_idHaveOnlyNumbers(MockMvc mvc) throws Exception {
		String uri = "/metrics?evaluated_id={evaluated_id}";
		MvcResult mvcResult = mvcGetTypeId(mvc, uri, "12345432");
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
		
	}

	public void getMetricsSprint_idHaveOnlyNumbers(MockMvc mvc) throws Exception {
		String uri = "/metrics?sprint_id={sprint_id}";
		MvcResult mvcResult = mvcGetTypeId(mvc, uri, "12345432");
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
	}

	
}
