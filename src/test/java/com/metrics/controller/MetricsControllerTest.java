package com.metrics.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.metrics.MetricsApplication;
import com.metrics.controller.MetricsController;
import com.metrics.controller.ControllerFunctions.FunctionsEnhanceGetPaginationTests;
import com.metrics.controller.ControllerFunctions.FunctionsEnhanceGetTest;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.model.blockers;
import com.metrics.model.blockersString;
import com.metrics.model.metrics;
import com.metrics.model.metricsString;
import com.metrics.model.proactive;
import com.metrics.model.proactiveString;
import com.metrics.model.retroactive;
import com.metrics.model.retroactiveString;
import com.metrics.service.TechnicalValidations;
import com.metrics.service.StaticFunctionsVariables.StaticVariables;
import org.junit.runners.MethodSorters;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MetricsApplication.class)
@AutoConfigureMockMvc

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MetricRepositoryTest {

	MetricsController metricsController = new MetricsController();
	FunctionsEnhanceGetPaginationTests testEnhanceGetPagination = new FunctionsEnhanceGetPaginationTests();
	FunctionsEnhanceGetTest testEnhanceGet = new FunctionsEnhanceGetTest();
	MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@BeforeEach
	protected void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	@DisplayName("test to method POST expected 201")
	public void a() throws Exception {
		CreateMetricRequest metric = newCreateMetricTRequest();

		MvcResult mvcResult = mvc
						.perform(MockMvcRequestBuilders.post("/metrics").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON).content(TechnicalValidations.mapToJson(metric))).andReturn();
		System.out.println("******************** The metric was created the id..: " + StaticVariables.id + " had been asigned *********************");
		assertEquals(201, mvcResult.getResponse().getStatus());

	}
	//In this test the evaluated_id, evaluator_id and sprint_id have the same behavior and this situation works to PUT method too
	@Test
	@DisplayName("test to method POST or PUT with invalid evaluated_id expected 409")
	public void aa() throws Exception {
		CreateMetricRequest metric = newWrongCreateMetricTRequest();

		MvcResult mvcResult = mvc
						.perform(MockMvcRequestBuilders.post("/metrics").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON).content(TechnicalValidations.mapToJson(metric))).andReturn();
		System.out.println("******************** The metric was created the id..: " + StaticVariables.id + " had been asigned *********************");
		assertEquals(409, mvcResult.getResponse().getStatus());

	}
	//this situation works to PUT method too
	@Test
	@DisplayName("test to method POST or PUT with id in body expected 400")
	public void aaa() throws Exception {
		CreateMetricRequest metric = newWrongBodyCreateMetricTRequest();

		MvcResult mvcResult = mvc
						.perform(MockMvcRequestBuilders.post("/metrics").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON).content(TechnicalValidations.mapToJson(metric))).andReturn();
		System.out.println("******************** The metric was created the id..: " + StaticVariables.id + " had been asigned *********************");
		assertEquals(400, mvcResult.getResponse().getStatus());

	}
	@Test
	@DisplayName("test to method GET by ID expected 200")
	public void b() throws Exception {
		String uri = "/metrics/{id}";
		System.out.println("******************** Trying to get element to DB with id..: " + StaticVariables.id + " *********************");
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.get(uri, StaticVariables.id).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertTrue(!content.isEmpty());
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		String expected = TechnicalValidations.mapToJson(ResponseCreateMetricRequest());
		assertEquals(expected, jsonResponse);
	}
	@Test
	@DisplayName("test to method PUT expected 200")
	public void c() throws Exception {
		
		CreateMetricRequest metric = newCreateMetricTRequest();
		System.out.println("******************** Trying change element with id..: " + StaticVariables.id + " *********************");
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put("/metrics/{id}", StaticVariables.id).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(TechnicalValidations.mapToJson(metric)))
				.andExpect(handler().handlerType(MetricsController.class))
				.andExpect(handler().methodName("updateMetric")).andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		String expected = TechnicalValidations.mapToJson(ResponseCreateMetricRequest());
		assertEquals(expected, jsonResponse);

	}
	@Test
	@DisplayName("test to method PUT expected 404")
	public void cc() throws Exception {
		
		CreateMetricRequest metric = newCreateMetricTRequest();
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put("/metrics/{id}", idFail).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(TechnicalValidations.mapToJson(metric)))
				.andExpect(handler().handlerType(MetricsController.class))
				.andExpect(handler().methodName("updateMetric")).andReturn();
		assertEquals(404, mvcResult.getResponse().getStatus());

	}
	
	@Test
	@DisplayName("test to method PUT expected 405")
	public void ccc() throws Exception {
		
		CreateMetricRequest metric = newCreateMetricTRequest();
		System.out.println("******************** Trying change element with id..:  *********************");
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.put("/metrics/{id}", "").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(TechnicalValidations.mapToJson(metric)))
				.andExpect(handler().handlerType(MetricsController.class))
				.andExpect(handler().methodName("not_allowed")).andReturn();
		assertEquals(405, mvcResult.getResponse().getStatus());

	}
	@Test
	@DisplayName("test to method DELETE expected 204")
	public void d() throws Exception {
		String uri = "/metrics/{id}";
		System.out.println("******************** Trying delete element with id..: " + StaticVariables.id + " *********************");
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri,  StaticVariables.id)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
	}


	@Test
	@DisplayName("test to method GET expected 200")
	public void e() throws Exception {
		String uri = "/metrics";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		MetricsCollection[] metricsCollection = TechnicalValidations.mapFromJson(content, MetricsCollection[].class);
		assertTrue(metricsCollection.length > 0);
	}
	

	@Test
	@DisplayName("test to method GET with pagination expected 200")
	public void f() throws Exception {

		testEnhanceGetPagination.getMetricsPagination(mvc);
	}
	@Test
	@DisplayName("test to method GET with Evaluator_ID expected 200")
	public void g() throws Exception {

		testEnhanceGet.getMetricsEvaluator_id(mvc);
	}
	@Test
	@DisplayName("test to method GET when ID content special characters expected 400")
	public void gg() throws Exception {

		testEnhanceGet.getMetricsEvaluator_idSC(mvc);
	}
	@Test
	@DisplayName("test to method GET with Evaluated_ID expected 200")
	public void h() throws Exception {

		testEnhanceGet.getMetricsEvaluated_id(mvc);
	}

	@Test
	@DisplayName("test to method GET with Sprint_ID expected 200")
	public void i() throws Exception {

		testEnhanceGet.getMetricsSprint_id(mvc);
	}
	@Test
	@DisplayName("test to method GET without Sprint_ID expected 200")
	public void ii() throws Exception {

		testEnhanceGet.getMetricsWithoutSprint_id(mvc);
	}
	@Test
	@DisplayName("test to method GET without Evaluated_id expected 200")
	public void iii() throws Exception {

		testEnhanceGet.getMetricsWithoutEvaluated_id(mvc);
	}
	
	@Test
	@DisplayName("test to method GET without Evaluator_id expected 200")
	public void iiii() throws Exception {

		testEnhanceGet.getMetricsWithoutEvaluator_id(mvc);
	}
	
	@Test
	@DisplayName("test to method GET all id's expected 200")
	public void iiiii() throws Exception {

		testEnhanceGet.getMetricsAllIds(mvc);
	}
	@Test
	@DisplayName("test to method GET with Dates expected 200")
	public void j() throws Exception {

		testEnhanceGet.getMetricsRangeDate(mvc);
	}
	@Test
	@DisplayName("test to method GET by non-exist ID expected 404")
	public void k() throws Exception {
		String uri = "/metrics/{id}";
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.get(uri, idFail).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
	}
	@Test
	@DisplayName("test to method GET by invalid ID expected 400")
	public void kk() throws Exception {
		String uri = "/metrics/{id}";
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.get(uri, "5e7a600a3b0").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}
	@Test
	@DisplayName("test to method PUT by non-exist ID expected 404")
	public void l() throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/metrics/{id}", idFail)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(TechnicalValidations.mapToJson(newCreateMetricTRequest())))
				.andExpect(handler().handlerType(MetricsController.class))
				.andExpect(handler().methodName("updateMetric")).andReturn();
		assertEquals(404, mvcResult.getResponse().getStatus());

	}

	
	@Test
	@DisplayName("test to method DELETE by invalid ID expected 400")
	public void m() throws Exception {
		String uri = "/metrics/{id}";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri, "5e7a600a3b0")).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}
	
	@Test
	@DisplayName("test to method DELETE by non-exist ID expected 404")
	public void mm() throws Exception {
		String uri = "/metrics/{id}";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri, idFail)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
	}
	
	@Test
	@DisplayName("test to method GET with wrong pagination expected 400")
	public void n() throws Exception {

		testEnhanceGetPagination.getMetricsPaginationFailSize(mvc);
	}
	@Test
	@DisplayName("test to method GET with letters in pagination request expected 400")
	public void nn() throws Exception {

		testEnhanceGetPagination.getMetricsPaginationFailType(mvc);
	}
	@Test
	@DisplayName("test to method GET with only numbers in evaluator_id expected 400")
	public void nnn() throws Exception {

		testEnhanceGetPagination.getMetricsEvaluator_idHaveOnlyNumbers(mvc);
	}
	@Test
	@DisplayName("test to method GET with only numbers in evaluated_id expected 400")
	public void nnnn() throws Exception {

		testEnhanceGetPagination.getMetricsEvaluated_idHaveOnlyNumbers(mvc);
	}
	@Test
	@DisplayName("test to method GET with only numbers in sprint_id expected 400")
	public void nnnnn() throws Exception {

		testEnhanceGetPagination.getMetricsSprint_idHaveOnlyNumbers(mvc);
	}
	@Test
	@DisplayName("test to method GET with pagination only page expected 200")
	public void o() throws Exception {

		testEnhanceGetPagination.getMetricsPaginationOnlyPage(mvc);
	}
	@Test
	@DisplayName("test to method GET with pagination only size expected 200")
	public void p() throws Exception {

		testEnhanceGetPagination.getMetricsPaginationOnlySize(mvc);
	}
	@Test
	@DisplayName("test to method GET with invalid evaluated_id expected 200")
	public void q() throws Exception {

		testEnhanceGet.geFailMetricsEvaluated_id(mvc);
	}

	@Test
	@DisplayName("test to method GET with invalid evaluator_id expected 200")
	public void r() throws Exception {

		testEnhanceGet.getFailMetricsEvaluator_id(mvc);

	}
	@Test
	@DisplayName("test to method POST or PUT with evaluator_id and evaluated are iquals expected 400")
	public void rr() throws Exception {

		CreateMetricRequest metric = newWrongIdsCreateMetricTRequest();

		MvcResult mvcResult = mvc
						.perform(MockMvcRequestBuilders.post("/metrics").contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON).content(TechnicalValidations.mapToJson(metric))).andReturn();
		System.out.println("******************** The metric was created the id..: " + StaticVariables.id + " had been asigned *********************");
		assertEquals(409, mvcResult.getResponse().getStatus());

	}

	@Test
	@DisplayName("test to method GET with invalid sprint_id expected 200")
	public void s() throws Exception {

		testEnhanceGet.getFailMetricsSprint_id(mvc);
	}
	@Test
	@DisplayName("test to method GET with letters in orderBy expected 400")
	public void t() throws Exception {

		testEnhanceGet.getFailOrderByHaveLetters(mvc);
	}
	@Test
	@DisplayName("test to method GET with letters in page expected 400")
	public void tt() throws Exception {

		testEnhanceGet.getFailPageHaveLetters(mvc);
	}
	@Test
	@DisplayName("test to method GET with letters in size expected 400")
	public void ttt() throws Exception {

		testEnhanceGet.getFailSizeHaveLetters(mvc);
	}
	@Test
	@DisplayName("test to method GET with invalit value in orderBy expected 400")
	public void u() throws Exception {

		testEnhanceGet.getFailInvalidOrderByValue(mvc);
	}
	@Test
	@DisplayName("test to method GET with orderBy ascending expected 200")
	public void uu() throws Exception {

		testEnhanceGet.getOrderByASC(mvc);
	}
	@Test
	@DisplayName("test to method GET when orderBy is out range expected 400")
	public void v() throws Exception {

		testEnhanceGet.getFailInvalidValueOrderBy(mvc);
	}
	@Test
	@DisplayName("test to method GET when page have an invalid value expected 400")
	public void vv() throws Exception {

		testEnhanceGet.getFailInvalidPageValue(mvc);
	}
	@Test
	@DisplayName("test to method GET when size have an invalid value expected 400")
	public void vvv() throws Exception {

		testEnhanceGet.getFailInvalidSizeValue(mvc);
	}
	@Test
	@DisplayName("test to method GET when startDate is bigger than endDate expected 400")
	public void w() throws Exception {

		testEnhanceGet.getFailDatesWrongOrder(mvc);
	}
	@Test
	@DisplayName("test to method GET date and day is out range expected 400")
	public void x() throws Exception {

		testEnhanceGet.getFailDatesWrongDay(mvc);
	}
	@Test
	@DisplayName("test to method GET date and month is out range expected 400")
	public void y() throws Exception {

		testEnhanceGet.getFailDatesWrongMonth(mvc);
	}
	@Test
	@DisplayName("test to method GET date had a invalid format expected 400")
	public void z() throws Exception {

		testEnhanceGet.getFailDatesWrongFormat(mvc);
	}
	private CreateMetricRequest newWrongCreateMetricTRequest() {
			return new CreateMetricRequest(StaticVariables.evaluator_id, StaticVariables.sprint_id, "Empty",
					"2020-03-17", StaticVariables.sprint_id,
					new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
							new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
		
	}
	private CreateMetricRequest newWrongBodyCreateMetricTRequest() {
			return new CreateMetricRequest(StaticVariables.id,StaticVariables.evaluator_id, StaticVariables.evaluated_id, "Empty",
					"2020-03-17", StaticVariables.sprint_id,
					new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
							new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	}
	private CreateMetricRequest newCreateMetricTRequest() {
			return new CreateMetricRequest(StaticVariables.evaluator_id, StaticVariables.evaluated_id, "Empty",
					"2020-03-17", StaticVariables.sprint_id,
					new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
							new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
	}
	private CreateMetricRequest newWrongIdsCreateMetricTRequest() {
			return new CreateMetricRequest(StaticVariables.evaluator_id, StaticVariables.evaluator_id, "Empty",
					"2020-03-17", StaticVariables.sprint_id,
					new metricsString("false", "false", new blockersString("false", "POST TESTV2 2020-03-17"),
							new proactiveString("false", "false", "false", "false"), new retroactiveString("false", "Empty")));
		
	}
	private MetricsCollection ResponseCreateMetricRequest() {

			return new MetricsCollection( StaticVariables.id, StaticVariables.evaluator_id, StaticVariables.evaluated_id, "Empty",
					"2020-03-17", StaticVariables.sprint_id,
					new metrics(false, false, new blockers(false, "POST TESTV2 2020-03-17"),
							new proactive(false, false, false, false), new retroactive(false, "Empty")));
	}
	String idFail = "5e7a600a3b09d6412bb1e663";
}
