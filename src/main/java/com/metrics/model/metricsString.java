package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiIgnore
public class metricsString {
	private String attendance;
	private String carried_over;
	private blockersString blockers;
	private proactiveString proactive;
	private retroactiveString retroactive;
}