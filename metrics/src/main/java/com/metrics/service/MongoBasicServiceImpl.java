package com.metrics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.metrics.repository.MetricRepository;

@Service
public class MongoBasicServiceImpl implements MongoBasicService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MongoBasicServiceImpl.class);
	
	@Autowired
	MetricRepository repository;

	
	 @Override
	 public ResponseEntity deleteMetric(String id) {
		 if (repository.existsById(id)) {
			 repository.deleteById(id);
			 return new ResponseEntity(HttpStatus.NO_CONTENT);
		 }
		 else {
			 return new ResponseEntity(HttpStatus.NOT_FOUND);
		 }
	 }
}
