package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.JsonHandler;
import com.safetynet.alerts.logic.parsers.CollectionParser;
import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.parsers.PersonAndRecordParser;
import com.safetynet.alerts.logic.service.GetService;
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
import static org.mockito.Mockito.doThrow;
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
    SafetyAlertsModel safetyAlertsModel;


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
        doThrow(new Exception("Exception")).when(safetyAlertsModel).loadModelFromDisk();

        //Method
        ResponseEntity<String> output = getMappingController.getChildrenAtAddress("address");

        //Verification
        int status = output.getStatusCode().value();

        assertEquals(500, status);
    }

    @Test
    public void firestationControllerThrowsError500IfDataFileUnavailable() throws Exception {
        //Preparation
        doThrow(new Exception("Exception")).when(safetyAlertsModel).loadModelFromDisk();

        //Method
        ResponseEntity<String> output = firestationController.deleteEntity("address", 10);

        //Verification
        int status = output.getStatusCode().value();
        assertEquals(500, status);
    }

    @Test
    public void medicalRecordControllerThrowsError500IfDataFileUnavailable() throws Exception {
        //Preparation
        doThrow(new Exception("Exception")).when(safetyAlertsModel).loadModelFromDisk();

        //Method
        ResponseEntity<String> output = medicalRecordController.deleteEntity("fake", "fake");

        //Verification
        int status = output.getStatusCode().value();
        assertEquals(500, status);
    }

    @Test
    public void personControllerThrowsError500IfDataFileUnavailable() throws Exception {
        //Preparation
        doThrow(new Exception("Exception")).when(safetyAlertsModel).loadModelFromDisk();

        //Method
        ResponseEntity<String> output = personController.deleteEntity("fake", "fake");

        //Verification
        int status = output.getStatusCode().value();
        assertEquals(500, status);
    }

}
