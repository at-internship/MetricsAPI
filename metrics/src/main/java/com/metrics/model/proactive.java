package com.metrics.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class proactive {
	private boolean looked_for_help;
	private boolean provided_help;
	private boolean worked_ahead;
	private boolean shared_resource;
	
}