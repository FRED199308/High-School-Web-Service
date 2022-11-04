package com.lunartech.HighSchoolProWebService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HighSchoolProWebServiceApplication extends SpringBootServletInitializer {

	public static   void main(String[] args) {
		SpringApplication.run(HighSchoolProWebServiceApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HighSchoolProWebServiceApplication.class);
	}
}
