package com.metrics.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

 

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
 	   public void deleteMetric() throws Exception {
 	      String uri = "/metrics/";
 	      MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
 	      int status = mvcResult.getResponse().getStatus();
 	      assertEquals(204, status);
 	   }
}