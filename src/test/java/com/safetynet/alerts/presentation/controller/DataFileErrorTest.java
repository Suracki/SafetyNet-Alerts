package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.JsonHandler;
import com.safetynet.alerts.logic.parsers.CollectionParser;
import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.parsers.PersonAndRecordParser;
import com.safetynet.alerts.logic.service.FirestationService;
import com.safetynet.alerts.logic.service.GetService;
import com.safetynet.alerts.logic.service.MedicalRecordService;
import com.safetynet.alerts.logic.service.PersonService;
import com.safetynet.alerts.logic.updaters.UpdateFirestation;
import com.safetynet.alerts.logic.updaters.UpdateMedicalRecord;
import com.safetynet.alerts.logic.updaters.UpdatePerson;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataFileErrorTest {

    @Mock
    JsonHandler jsonHandler;
    @Mock
    JsonDAO jsonDAO;
    @Mock
    ModelObjectFinder finder;
    @Mock
    CollectionParser parser;
    @Mock
    PersonAndRecordParser recordParser;
    @Mock
    GetService getService;
    @Mock
    DataConfig dataConfig;
    @Mock
    LogHandlerTiny logHandlerTiny;
    @Mock
    SafetyAlertsModel safetyAlertsModel;
    @Mock
    PersonService personService;
    @Mock
    MedicalRecordService medicalRecordService;
    @Mock
    FirestationService firestationService;


    @InjectMocks
    GetMappingController getMappingController;

    @InjectMocks
    MedicalRecordController medicalRecordController;
    @InjectMocks
    PersonController personController;
    @InjectMocks
    FirestationController firestationController;

    @Test
    public void getMappingControllerThrowsError500IfDataFileUnavailable() throws Exception {
        //Preparation
        doReturn(false).when(getService).isDataLoaded();

        //Method
        ResponseEntity<String> output = getMappingController.getChildrenAtAddress("address");

        //Verification
        int status = output.getStatusCode().value();

        assertEquals(500, status);
    }

    @Test
    public void firestationControllerThrowsError500IfDataFileUnavailable() throws Exception {
        //Preparation
        doReturn(false).when(firestationService).isDataLoaded();

        //Method
        ResponseEntity<String> output = firestationController.deleteEntity("address", 10);

        //Verification
        int status = output.getStatusCode().value();
        assertEquals(500, status);
    }

    @Test
    public void medicalRecordControllerThrowsError500IfDataFileUnavailable() throws Exception {
        //Preparation
        doReturn(false).when(medicalRecordService).isDataLoaded();

        //Method
        ResponseEntity<String> output = medicalRecordController.deleteEntity("fake", "fake");

        //Verification
        int status = output.getStatusCode().value();
        assertEquals(500, status);
    }

    @Test
    public void personControllerThrowsError500IfDataFileUnavailable() throws Exception {
        //Preparation
        doReturn(false).when(personService).isDataLoaded();

        //Method
        ResponseEntity<String> output = personController.deleteEntity("fake", "fake");

        //Verification
        int status = output.getStatusCode().value();
        assertEquals(500, status);
    }

}
