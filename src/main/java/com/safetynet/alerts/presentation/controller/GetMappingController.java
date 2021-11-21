package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.CollectionParser;
import com.safetynet.alerts.logic.GetService;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.logic.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class GetMappingController {

    private LogHandlerTiny logHandler;
    private JsonHandler jsonHandler;
    private JsonDAO jsonDAO;
    private ModelObjectFinder finder;
    private CollectionParser parser;
    private PersonAndRecordParser recordParser;
    private GetService getService;
    private DataConfig dataConfig;

    @Autowired
    public GetMappingController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder, CollectionParser parser,
                                PersonAndRecordParser recordParser, GetService getService,
                                DataConfig dataConfig, LogHandlerTiny logHandler) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.parser = parser;
        this.recordParser = recordParser;
        this.getService = getService;
        this.dataConfig = dataConfig;
        this.logHandler = logHandler;
    }

    private SafetyAlertsModel loadModelFromDisk() {
        try {
            return jsonHandler.jsonToModel(jsonDAO.readJsonFromFile(dataConfig.getDataFile()));
        }
        catch (Exception e) {
            logHandler.setLogger("GetMappingController");
            logHandler.error("Error loading database file " + e);
        }
        return null;
    }

    private String[] intArrayToStringArray(int[] intArray) {
        return Arrays.stream(intArray).mapToObj(String::valueOf).toArray(String[]::new);
    }

    @GetMapping("/firestation")
    public ResponseEntity<String> getPeopleServicedByStation(@RequestParam("stationNumber") int stationNumber) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/firestation", new String[] {String.valueOf(stationNumber)});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform request
        ResponseEntity<String> response = getService.getPeopleServicedByStationEntity(stationNumber, model, finder,
                parser, recordParser, logHandler);
        //Log response
        logHandler.logResponse("GET", response);
        //Respond
        return response;

    }

    @GetMapping("/childAlert")
    public ResponseEntity<String> getChildrenAtAddress(@RequestParam("address") String address) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/childAlert", new String[] {address});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getChildrenAtAddressEntity(address, model, finder, recordParser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);

        //Respond
        return response;
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<String> getPhoneNumbersForPeopleServicedByStation(@RequestParam("firestation") int stationNumber) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/phoneAlert", new String[] {String.valueOf(stationNumber)});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getPhoneNumbersForPeopleServicedByStationEntity(stationNumber, model,
                finder, parser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);

        //Respond
        return response;

    }

    @GetMapping("/fire")
    public ResponseEntity<String> getFirestationNumberAndResidentsForAddress(@RequestParam("address") String address) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/fire", new String[] {address});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getFirestationNumberAndResidentsForAddressEntity(address, model, finder,
                parser, recordParser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);
        //respond
        return response;
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<String> getHouseholdsByFirestation(@RequestParam("stations") int[] stationNumbers) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/flood/stations", intArrayToStringArray(stationNumbers));
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getHouseholdsByFirestationEntity(stationNumbers, model, finder,
                parser, recordParser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);
        //respond
        return response;
    }

    @GetMapping("/personInfo")
    public ResponseEntity<String> getPersonInfoByFirstNameLastName(@RequestParam("firstName") String firstName,
                                                                   @RequestParam("lastName") String lastName) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/personInfo", new String[] {firstName, lastName});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getPersonInfoByFirstNameLastNameEntity(firstName, lastName, model, finder,
                                                                                            recordParser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);
        //respond
        return response;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<String> getEmailAddressesByCity(@RequestParam("city") String city) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/communityEmail", new String[] {city});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getEmailAddressesByCity(city, model, finder, logHandler);

        //Log response
        logHandler.logResponse("GET", response);
        //respond
        return response;
    }

}