package com.example.demo;

import com.example.demo.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.demo")
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

		// Test if UserService bean is available
		try {
			UserService userService = context.getBean(UserService.class);
			System.out.println("UserService bean initialized successfully.");
		} catch (Exception e) {
			System.err.println("Failed to initialize UserService: " + e.getMessage());
		}
	}
}
