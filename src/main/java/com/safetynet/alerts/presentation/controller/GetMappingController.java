package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logic.CollectionParser;
import com.safetynet.alerts.logic.GetService;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.logic.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GetMappingController {

    private JsonHandler jsonHandler;
    private JsonDAO jsonDAO;
    private ModelObjectFinder finder;
    private CollectionParser parser;
    private PersonAndRecordParser recordParser;
    private OutputBuilder builder;
    private GetService getService;
    private DataConfig dataConfig;

    @Autowired
    public GetMappingController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder, CollectionParser parser,
                                PersonAndRecordParser recordParser, OutputBuilder builder, GetService getService, DataConfig dataConfig) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.parser = parser;
        this.recordParser = recordParser;
        this.builder = builder;
        this.getService = getService;
        this.dataConfig = dataConfig;
    }

    private SafetyAlertsModel loadModelFromDisk() {
        try {
            //TODO
            //Add config file to change prod/dev file names
            return jsonHandler.jsonToModel(jsonDAO.readJsonFromFile(dataConfig.getDataFile()));
        }
        catch (Exception e) {
            System.out.println("Error loading database: " + e);
        }
        return null;
    }

    @GetMapping("/firestation")
    public ResponseEntity<String> getPeopleServicedByStation(@RequestParam("stationNumber") int stationNumber) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform request
        ResponseEntity<String> response = getService.getPeopleServicedByStation(stationNumber, model, finder,
                                                                                parser, builder, recordParser);
        //Respond
        return response;

    }

    @GetMapping("/childAlert")
    public ResponseEntity<String> getChildrenAtAddress(@RequestParam("address") String address) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        ResponseEntity<String> response = getService.getChildrenAtAddress(address, model, finder, builder, recordParser);

        //Respond
        return response;
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<String> getPhoneNumbersForPeopleServicedByStation(@RequestParam("firestation") int stationNumber) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        ResponseEntity<String> response = getService.getPhoneNumbersForPeopleServicedByStation(stationNumber, model,
                finder, builder, parser);

        //Respond
        return response;

    }

    @GetMapping("/fire")
    public ResponseEntity<String> getFirestationNumberAndResidentsForAddress(@RequestParam("address") String address) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        ResponseEntity<String> response = getService.getFirestationNumberAndResidentsForAddress(address, model, finder,
                builder, parser, recordParser);

        //respond
        return response;
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<String> getHouseholdsByFirestation(@RequestParam("stations") int[] stationNumbers) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        ResponseEntity<String> response = getService.getHouseholdsByFirestation(stationNumbers, model, finder, builder,
                parser, recordParser);


        //respond
        return response;
    }

    @GetMapping("/personInfo")
    public ResponseEntity<String> getPersonInfoByFirstNameLastName(@RequestParam("firstName") String firstName,
                                                                   @RequestParam("lastName") String lastName) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        ResponseEntity<String> response = getService.getPersonInfoByFirstNameLastName(firstName, lastName, model, finder, builder);

        //respond
        return response;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<String> getEmailAddressesByCity(@RequestParam("city") String city) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        ResponseEntity<String> response = getService.getEmailAddressesByCity(city, model, finder, builder);

        //respond
        return response;
    }

}