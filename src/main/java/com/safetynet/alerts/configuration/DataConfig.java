package com.safetynet.alerts.configuration;

import org.springframework.stereotype.Service;

@Service
public class DataConfig {
    private String dataFile;

    public DataConfig() {
        dataFile = "database/livedata.json";
    }

    public DataConfig(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getDataFile() {
        return dataFile;
    }

}
