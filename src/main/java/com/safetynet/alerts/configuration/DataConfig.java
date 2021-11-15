package com.safetynet.alerts.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataConfig {
    private String dataFile;

    @Value( "${app.environment}" )
    private String appEnvironment;


    @Autowired
    public DataConfig(@Value( "${app.environment}" ) String environment) {
        if (environment.equals("prod")) {
            dataFile = "database/livedata.json";
        }
        else if (environment.equals("dev")) {
            dataFile = "database/testdata.json";
        }
        else {
            //default to test database in case of issues with properties file
            dataFile = "database/testdata.json";
        }

    }

//    public DataConfig(String dataFile) {
//        this.dataFile = dataFile;
//    }

    public String getDataFile() {
        return dataFile;
    }

}
