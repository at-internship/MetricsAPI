package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class metrics {
	private Boolean attendance;
	private Boolean carried_over;
	private blockers blockers;
	private proactive proactive;
	private retroactive retroactive;
}