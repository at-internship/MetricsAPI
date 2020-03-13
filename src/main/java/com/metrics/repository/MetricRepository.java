package com.metrics.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.metrics.model.MetricsCollection;

public interface MetricRepository extends MongoRepository<MetricsCollection, String> {
	List<MetricsCollection> findAll();
	MetricsCollection findById();
}