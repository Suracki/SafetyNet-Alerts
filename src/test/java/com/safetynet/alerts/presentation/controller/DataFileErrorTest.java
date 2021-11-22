package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.*;
import com.safetynet.alerts.logic.JsonHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
    UpdateFirestation updateFirestation;
    @Mock
    UpdateMedicalRecord updateMedicalRecord;
    @Mock
    UpdatePerson updatePerson;

    GetMappingController getMappingController;
    FirestationController firestationController;
    MedicalRecordController medicalRecordController;
    PersonController personController;

    @Test
    public void getMappingControllerThrowsError500IfDataFileUnavailable()  {
        //Preparation
        getMappingController = new GetMappingController(jsonHandler, jsonDAO, finder, parser, recordParser, getService, dataConfig, logHandlerTiny);
        when(jsonHandler.jsonToModel(Mockito.anyString())).thenThrow(new RuntimeException("Exception"));

        //Method
        ResponseEntity<String> output = getMappingController.getChildrenAtAddress("address");

        //Verification
        int status = output.getStatusCode().value();

        assertEquals(500, status);
    }

    @Test
    public void firestationControllerThrowsError500IfDataFileUnavailable()  {
        //Preparation
        firestationController = new FirestationController(jsonHandler, jsonDAO, finder, updateFirestation, dataConfig, logHandlerTiny);
        when(jsonHandler.jsonToModel(Mockito.anyString())).thenThrow(new RuntimeException("Exception"));

        //Method
        ResponseEntity<String> output = firestationController.deleteEntity("address", 10);

        //Verification
        int status = output.getStatusCode().value();

        assertEquals(500, status);
    }

    @Test
    public void medicalRecordControllerThrowsError500IfDataFileUnavailable()  {
        //Preparation
        medicalRecordController = new MedicalRecordController(jsonHandler, jsonDAO, finder, updateMedicalRecord, dataConfig, logHandlerTiny);
        when(jsonHandler.jsonToModel(Mockito.anyString())).thenThrow(new RuntimeException("Exception"));

        //Method
        ResponseEntity<String> output = medicalRecordController.deleteEntity("fake", "fake");

        //Verification
        int status = output.getStatusCode().value();

        assertEquals(500, status);
    }

    @Test
    public void personControllerThrowsError500IfDataFileUnavailable()  {
        //Preparation
        personController = new PersonController(jsonHandler, jsonDAO, finder, updatePerson, dataConfig, logHandlerTiny);
        when(jsonHandler.jsonToModel(Mockito.anyString())).thenThrow(new RuntimeException("Exception"));

        //Method
        ResponseEntity<String> output = personController.deleteEntity("fake", "fake");

        //Verification
        int status = output.getStatusCode().value();

        assertEquals(500, status);
    }

}
