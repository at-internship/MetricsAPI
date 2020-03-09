
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
	
}