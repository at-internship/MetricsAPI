package com.metrics.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.metrics.model.MetricsCollection;
import com.metrics.service.Functions;

public class FunctionsEnhanceGetPaginationTests {
	public void getMetricsPagination(MockMvc mvc) throws Exception {
		int size = 4;
		int start = 1;
		String uri = "/metrics?start={start}&size={size}";
		MvcResult mvcResult = mvcGetPaginationRequest(mvc, uri, start, size);

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	public void getMetricsPaginationFailSize(MockMvc mvc) throws Exception {
		int size = 1000;
		int start = 1;
		String uri = "/metrics?start={start}&size={size}";
		MvcResult mvcResult = mvcGetPaginationRequest(mvc, uri, start, size);

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}

	public void getMetricsPaginationFailStart(MockMvc mvc) throws Exception {
		int size = 2;
		int start = 1000;
		String uri = "/metrics?start={start}&size={size}";
		MvcResult mvcResult = mvcGetPaginationRequest(mvc, uri, start, size);

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}

	private MvcResult mvcGetPaginationRequest(MockMvc mvc, String uri, int start, int size) throws Exception {
		return mvc.perform(MockMvcRequestBuilders.get(uri, start, size).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
}
