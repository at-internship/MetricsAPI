package com.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Data
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