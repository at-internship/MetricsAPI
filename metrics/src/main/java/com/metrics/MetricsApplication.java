package com.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetricsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);
	}
	
	
	/*
	 {
            "evaluator_id" : "name",
            "evaluated_id" : "looolo",
            "type" : "String",
            "date" : "1000-01-01",
            "sprint_id" : "UUID",
            "metrics" :
                    {
                "attendance" : "false",
                "blockers" : {
                    "blocked" : "false",
                    "comments" : "String"
                },
                "proactive" : {
                    "looked_for_help" : "false",
                    "provided_help" : "false",
                    "worked_ahead" : "false",
                    "shared_resources": "false"
                },
                "carried_over" : "false",
                "retroactive" : {
                    "delayed_looking_help" : "false",
                    "comments" : "String"
                }
            }
        }
	 */

}
