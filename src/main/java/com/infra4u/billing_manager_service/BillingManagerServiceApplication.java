package com.infra4u.billing_manager_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BillingManagerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingManagerServiceApplication.class, args);
	}

}
