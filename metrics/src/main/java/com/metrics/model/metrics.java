package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Data
public class metrics {
	private boolean attendance;
	private boolean carried_over;
	private blockers blockers;
	private proactive proactive;
	private retroactive retroactive;
}