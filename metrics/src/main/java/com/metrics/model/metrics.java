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
}