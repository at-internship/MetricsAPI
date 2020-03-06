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
public class retroactive {
	private boolean delayed_looking_help;
	private String comments;
	
	public retroactive(boolean delayed_looking_help, String comments) {
		this.delayed_looking_help = delayed_looking_help;
		this.comments = comments;
	}
}
