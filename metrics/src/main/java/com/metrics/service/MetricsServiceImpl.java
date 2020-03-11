package com.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;
import com.metrics.repository.MetricRepository;

import ma.glasnost.orika.MapperFacade;

@Service
public class MetricsServiceImpl implements MetricsService {
	@Autowired
	MetricRepository repository;

	@Autowired
	private MapperFacade orikaMapperFacade;
	
	@Override
	public MetricsCollection updateMetric(CreateMetricRequest request, String id) {
		if (!repository.existsById(id))
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user found with the given id");
		MetricsCollection metric = new MetricsCollection();
		metric = orikaMapperFacade.map(request, MetricsCollection.class);
		metric.setId(id);
		return repository.save(metric);
	}
	@Override
	public void SetDefaultDataEmptyField(CreateMetricRequest metric) {
		if(metric.getMetrics().isAttendance() == (Boolean) null) {
			metric.getMetrics().setAttendance(false);
		}
		if(metric.getMetrics().isCarried_over() == (Boolean) null) {
			metric.getMetrics().setCarried_over(false);
		}
		if(metric.getMetrics().getBlockers().isBlocked() == (Boolean) null) {
			metric.getMetrics().getBlockers().setBlocked(false);
		}
		if(metric.getMetrics().getProactive().isLooked_for_help() == (Boolean) null) {
			metric.getMetrics().getProactive().setLooked_for_help(false);;
		}
		if(metric.getMetrics().getProactive().isProvided_help() == (Boolean) null) {
			metric.getMetrics().getProactive().setProvided_help(false);
		}
		if(metric.getMetrics().getProactive().isShared_resources() == (Boolean) null) {
			metric.getMetrics().getProactive().setShared_resources(false);
		}
		if(metric.getMetrics().getProactive().isWorked_ahead() == (Boolean) null) {
			metric.getMetrics().getProactive().setWorked_ahead(false);
		}
		if(metric.getMetrics().getRetroactive().isDelayed_looking_help() == (Boolean) null) {
			metric.getMetrics().getRetroactive().setDelayed_looking_help(false);
		}
	}
}
