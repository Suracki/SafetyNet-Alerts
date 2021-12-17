package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.parsers.CollectionParser;
import com.safetynet.alerts.logic.service.GetService;
import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.parsers.PersonAndRecordParser;
import com.safetynet.alerts.logic.JsonHandler;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

public class GetControllerBeanSetup {

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
    public CollectionParser parser() {
        return new CollectionParser();
    }
    @Bean
    public PersonAndRecordParser recordParser() {
        return new PersonAndRecordParser();
    }
    @Bean
    public GetService getService() {
        return new GetService();
    }
    @Bean
    public DataConfig dataConfig() {
        return new DataConfig("database/testdata.json");
    }
    @Bean
    public SafetyAlertsModel safetyAlertsModel() { return new SafetyAlertsModel();}
    @MockBean
    LogHandlerTiny logHandlerTiny;

}
