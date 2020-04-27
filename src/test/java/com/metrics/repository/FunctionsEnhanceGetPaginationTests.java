package com.metrics.repository;

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
}
