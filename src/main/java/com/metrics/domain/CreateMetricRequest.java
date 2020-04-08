package com.metrics.domain;

import javax.persistence.Id;

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
	@Id
	private String id;
	private String evaluator_id;
	private String evaluated_id;
	private String type;
	private String date;
	private String sprint_id;
	private metrics metrics;

	public CreateMetricRequest(String type, String date, com.metrics.model.metrics metrics) {
		this.type = type;
		this.date = date;
		this.metrics = metrics;
	}

	public CreateMetricRequest(String evaluator_id, String evaluated_id, String type, String sprint_id, com.metrics.model.metrics metrics) {
		this.evaluator_id = evaluator_id;
		this.evaluated_id = evaluated_id;
		this.type = type;
		this.sprint_id = sprint_id;
		this.metrics = metrics;
	}

}