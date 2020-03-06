package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class proactive {
	private boolean looked_for_help;
	private boolean provided_help;
	private boolean worked_ahead;
	private boolean shared_resource;
	
	public proactive(boolean looked_for_help, 
					 boolean provided_help, 
					 boolean worked_ahead, 
					 boolean shared_resource) {
		this.looked_for_help = looked_for_help;
		this.provided_help = provided_help;
		this.worked_ahead = worked_ahead;
		this.shared_resource = shared_resource;
	}
}
