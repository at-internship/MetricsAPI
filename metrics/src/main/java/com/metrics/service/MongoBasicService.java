package com.metrics.service;

import org.springframework.http.ResponseEntity;

public interface MongoBasicService {

	ResponseEntity deleteMetric(String id);
}
