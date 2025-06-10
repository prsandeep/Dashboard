package com.bugzilla.bugzilla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class BugzillaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BugzillaApplication.class, args);
	}
	
	 // Configure CORS to allow requests from the React frontend
	
    
}
