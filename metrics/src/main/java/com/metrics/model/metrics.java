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
public class metrics {
	private boolean attendance;
	private boolean carried_over;
	private blockers blockers;
	private proactive proactive;
	private retroactive retroactive;
	
	public metrics(boolean attendance, boolean carried_over, 
			com.metrics.model.blockers blockers, 
			com.metrics.model.proactive proactive,
			com.metrics.model.retroactive retroactive) {
		this.attendance = attendance;
		this.carried_over = carried_over;
		this.blockers = blockers;
		this.proactive = proactive;
		this.retroactive = retroactive;
	}
}
