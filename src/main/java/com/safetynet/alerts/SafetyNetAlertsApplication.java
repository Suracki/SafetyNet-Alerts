package com.safetynet.alerts;

import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SafetyNetAlertsApplication {

	private SafetyAlertsModel safetyAlertsModel;

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApplication.class, args);
	}

}
