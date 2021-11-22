package com.safetynet.alerts.presentation.actuator;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Implementation of health SpringBoot endpoint
 */
@Component
public class HealthCheck implements HealthIndicator {

    @Autowired
    JsonDAO jsonDAO;

    @Autowired
    DataConfig dataConfig;

    @Override
    public Health health() {
        try {
            jsonDAO.readJsonFromFile(dataConfig.getDataFile());
        }
        catch (Exception e) {
            return Health.down().withDetail("Cause", "Data file" + dataConfig.getDataFile() + " is not available.").build();
        }
        return Health.up().build();
    }
}