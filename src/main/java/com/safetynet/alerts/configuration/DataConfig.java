package com.safetynet.alerts.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * DataConfig class is used to determine which database file to load
 *
 * Reads app.environment variable from application.properties
 */
@Service
public class DataConfig {
    private String dataFile;

    @Value( "${app.environment}" )
    private String appEnvironment;


    /**
     * Consutrctor for DataConfig
     *
     * @param environment either 'prod' or 'dev', autofilled from app.environment file
     */
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


    /**
     * Accessor method for Datafile location String for use in reading/writing files
     *
     * @return dataFile location String
     */
    public String getDataFile() {
        return dataFile;
    }

}
