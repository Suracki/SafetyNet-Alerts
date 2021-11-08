package com.safetynet.alerts;

import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetyNetAlertsApplication {

	private SafetyAlertsModel safetyAlertsModel;

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApplication.class, args);
	}

}
