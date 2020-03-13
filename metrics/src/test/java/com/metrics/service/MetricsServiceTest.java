package com.metrics.service;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.metrics.AbstractTest;
import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
@Transactional
public class MetricsServiceTest extends AbstractTest {

	@Autowired
	MetricsServiceImpl service;
	@Autowired
	CreateMetricRequest request;
	@Autowired
	MetricsCollection actualMetric;
	
	@Test
	public void testNewMetric()
	{
		
		actualMetric = service.newMetric(request);
		
		Assert.assertNotNull("Faiulre - Expected not null", request);
		Assert.assertEquals("failure - expected metric object", MetricsCollection.class , actualMetric);
		
	}
	
		
}
