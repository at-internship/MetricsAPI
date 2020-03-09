package com.metrics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.metrics.model.MetricsCollection;

public interface MetricRepository extends MongoRepository<MetricsCollection, String>{
	void deleteByID(String id);	
}
