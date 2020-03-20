package com.metrics.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.MetricsApplication;
import com.metrics.controller.MetricsController;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.blockers;
import com.metrics.model.metrics;
import com.metrics.model.proactive;
import com.metrics.model.retroactive;
import com.metrics.service.Functions;
import com.metrics.service.MetricsServiceImpl;

 

 

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MetricsApplication.class)
@AutoConfigureMockMvc 
class MetricRepositoryTest {
     MockMvc  mvc;
    @Autowired
       WebApplicationContext webApplicationContext;
        @BeforeEach
       protected void setUp() {
           mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
       }

 

    
    @Test
    public void test_update_user_success() throws Exception {
    	
        CreateMetricRequest metric = newCreateMetricRequest();
        
        assertTrue(ObjectId.isValid(metric.getId()));
        
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.
                put("/metrics/{id}", metric.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Functions.mapToJson(metric)))
                .andExpect(handler().handlerType(MetricsController.class))
                .andExpect(handler().methodName("updateMetric"))
                .andDo(print())
                .andReturn();
        assertEquals(202, mvcResult.getResponse().getStatus());
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonResponse, Functions.mapToJson(metric));
    }

 
@Test
     public void getMetricsList() throws Exception {
     String uri = "/metrics";
     MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
    		 .accept(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
             .andReturn();
           
     int status = mvcResult.getResponse().getStatus();
     assertEquals(200, status);
     String content = mvcResult.getResponse().getContentAsString();
     MetricsCollection[] metricsCollection = mapFromJson(content, MetricsCollection[].class);
     assertTrue(metricsCollection.length > 0);
     }
   
     @Test
     public void getMetricByIdTest() throws Exception {
     CreateMetricRequest metric = newCreateMetricRequest();
     
     String uri = "/metrics/{id}";
     MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri,metric.getId())
    		 .accept(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
             .andReturn();      
     int status = mvcResult.getResponse().getStatus();
     assertEquals(200, status);
     String content = mvcResult.getResponse().getContentAsString();
     assertTrue(!content.isEmpty());
     }
     
     @Test
     public void getMetricByIdTest_404_NOTFOUND() throws Exception {
     CreateMetricRequest metric = falseCreateMetricRequest();
     
     String uri = "/metrics/{id}";
     MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri,metric.getId())
    		 .accept(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
             .andReturn();      
     int status = mvcResult.getResponse().getStatus();
     assertEquals(404, status);
     String content = mvcResult.getResponse().getContentAsString();
     assertTrue(content.isEmpty());
     }
    
     @Test
    public void test_update_user_fail_404_not_found() throws Exception {
        CreateMetricRequest falseMetric = falseCreateMetricRequest();

        

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.
                put("/metrics/{id}", falseMetric.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Functions.mapToJson(falseMetric)))
                .andExpect(handler().handlerType(MetricsController.class))
                .andExpect(handler().methodName("updateMetric"))
                .andDo(print())
                .andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        assertEquals(jsonResponse, "");

        
    }

	@Test
	public void testPOSTMetric() throws Exception
	{
		
		CreateMetricRequest metric = newCreateMetricPOSTRequest();
        
        MvcResult result = mvc.perform(
        					MockMvcRequestBuilders.post("/metrics")
        						.contentType(MediaType.APPLICATION_JSON)
        						.accept(MediaType.APPLICATION_JSON).content(mapToJson(metric)))
        						.andReturn();            
       
        assertEquals(201, result.getResponse().getStatus());
        
	}
	
	@Test
	public void testWrongPOSTMetric() throws Exception
	{
		
		CreateMetricRequest metric = falseCreateMetricRequest();
        
        MvcResult result = mvc.perform(
        					MockMvcRequestBuilders.post("/metrics")
        						.contentType(MediaType.APPLICATION_JSON)
        						.accept(MediaType.APPLICATION_JSON).content(mapToJson(metric)))
        						.andReturn();  
       
        assertEquals(400, result.getResponse().getStatus());
        
	}
    
    private CreateMetricRequest newCreateMetricRequest () {
    	
    	try {
    		
    		return new CreateMetricRequest("5e71378c0d386b2e07b600dc","Empty","Empty","Empty","2020-03-17","Empty",new metrics(false,false,
                    new blockers(false,"POST TESTV2 2020-03-17"),
                    new proactive(false, false,false,false),
                    new retroactive(false,"Empty")));
    	}catch(Exception e)
    	{
    		
    		System.out.println("FALLO PARSEO");
    		return null;
    	}
        
    }
 
 private CreateMetricRequest newCreateMetricPOSTRequest () {
    	
    	try {
    		
    		return new CreateMetricRequest("","Empty","Empty","Empty","2020-03-17","Empty",new metrics(false,false,
                    new blockers(false,"POST TESTV2 2020-03-17"),
                    new proactive(false, false,false,false),
                    new retroactive(false,"Empty")));
    	}catch(Exception e)
    	{
    		
    		System.out.println("FALLO PARSEO");
    		return null;
    	}
 }
private CreateMetricRequest falseCreateMetricRequest () {
    	try {
    		
    		return new CreateMetricRequest("e60f029b807","Empty","Empty","Empty","DATE","Empty",new metrics(false,false,
                    new blockers(false,"Empty"),
                    new proactive(false, false,false,false),
                    new retroactive(false,"Empty")));
    	}catch(Exception e)
    	{
    		return null;
    	}
    }
   
    
    protected String mapToJson(Object obj) throws JsonProcessingException {
          ObjectMapper objectMapper = new ObjectMapper();
          return objectMapper.writeValueAsString(obj);
       }
}