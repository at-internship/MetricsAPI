package com.metrics;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class MetricsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);
		/*
		String dataParseTest = "{\n" +
                "            \"evaluator_id\" : \"name\",\n" +
                "            \"evaluated_id\" : \"looolo\",\n" +
                "            \"type\" : \"String\",\n" +
                "            \"date\" : \"1000-01-01\",\n" +
                "            \"sprint_id\" : \"UUID\",\n" +
                "            \"metrics\" :  \n" +
                "                    {\n" +
                "                \"attendance\" : \"false\",\n" +
                "                \"blockers\" : {\n" +
                "                    \"blocked\" : \"false\",\n" +
                "                    \"comments\" : \"String\"\n" +
                "                },\n" +
                "                \"proactive\" : {\n" +
                "                    \"looked_for_help\" : \"false\",\n" +
                "                    \"provided_help\" : \"false\",\n" +
                "                    \"worked_ahead\" : \"false\",\n" +
                "                    \"shared_resources\": \"false\"\n" +
                "                },\n" +
                "                \"carried_over\" : \"false\",\n" +
                "                \"retroactive\" : {\n" +
                "                    \"delayed_looking_help\" : \"false\",\n" +
                "                    \"comments\" : \"String\"\n" +
                "                }\n" +
                "            }\n" +
                "        }";
		ObjectMapper mapper = new ObjectMapper();
		try {
				MetricsCollection metricsCollection = mapper.readValue(dataParseTest, MetricsCollection.class);
				/*
				System.out.println(metricsCollection.getEvaluated_id());
				System.out.println(metricsCollection.getEvaluator_id());
				System.out.println(metricsCollection.getSprint_id());
				System.out.println(metricsCollection.getDate());
				*/
			}
			
		/*
		catch (IOException e) 
		{
			e.printStackTrace();
			//new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
			
	
		}
		*/
			
	}



