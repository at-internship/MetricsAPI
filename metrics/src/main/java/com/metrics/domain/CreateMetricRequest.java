package com.metrics.domain;

import com.metrics.model.metrics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateMetricRequest {

	private String id;
	private String evaluator_id;
	private String evaluated_id;
	private String type;
	private String date;
	private String sprint_id;
	private metrics metrics;
	
	public CreateMetricRequest(String type, 
							 String date,
							 com.metrics.model.metrics metrics) {
		this.type = type;
		this.date = date;
		this.metrics = metrics;
	}
}
