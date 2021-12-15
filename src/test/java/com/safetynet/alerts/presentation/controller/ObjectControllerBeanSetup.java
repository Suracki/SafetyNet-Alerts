package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.JsonHandler;
import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.service.PersonService;
import com.safetynet.alerts.logic.updaters.UpdateFirestation;
import com.safetynet.alerts.logic.updaters.UpdateMedicalRecord;
import com.safetynet.alerts.logic.updaters.UpdatePerson;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    @Bean
    public PersonService personService() {
        return new PersonService();
    }
    @Bean
    public SafetyAlertsModel safetyAlertsModel() {return new SafetyAlertsModel();}
    @MockBean
    public LogHandlerTiny logHandlerTiny;

}
