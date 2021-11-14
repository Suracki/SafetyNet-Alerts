package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logic.*;
import com.safetynet.alerts.presentation.model.JsonHandler;
import org.springframework.context.annotation.Bean;

public class ObjectControllerBeanSetup {

    @Bean
    public JsonHandler jsonHandler() {
        return new JsonHandler();
    }
    @Bean
    public JsonDAO jsonDAO() {
        return new JsonDAO();
    }
    @Bean
    public ModelObjectFinder finder() {
        return new ModelObjectFinder();
    }
    @Bean
    public DataConfig dataConfig() {
        return new DataConfig("database/testdata.json");
    }
    @Bean
    public UpdatePerson updatePerson() {
        return new UpdatePerson();
    }
    @Bean
    public UpdateFirestation updateFirestation() {
        return new UpdateFirestation();
    }
    @Bean
    public UpdateMedicalRecord updateMedicalRecord() {
        return new UpdateMedicalRecord();
    }

}
