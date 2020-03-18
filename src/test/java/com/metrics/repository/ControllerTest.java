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
import com.metrics.model.MetricsCollection;
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

	
    
    
    private CreateMetricRequest newCreateMetricRequest () {
        return new CreateMetricRequest("5e691fd8cdcafe60f029b807","Empty","Empty","Empty","1001-01-02","Empty",new metrics(false,false,
                                    new blockers(false,"Empty"),
                                    new proactive(false, false,false,false),
                                    new retroactive(false,"Empty")));
    }
    
    private CreateMetricRequest falseCreateMetricRequest () {
        return new CreateMetricRequest("5e691cdc","Empty","Empty","Empty","1001-01-02","Empty",new metrics(false,false,
                                    new blockers(false,"Empty"),
                                    new proactive(false, false,false,false),
                                    new retroactive(false,"Empty")));
    }
}