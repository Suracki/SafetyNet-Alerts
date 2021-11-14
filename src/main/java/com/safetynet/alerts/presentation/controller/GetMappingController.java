package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logic.CollectionParser;
import com.safetynet.alerts.logic.GetService;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.logic.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class GetMappingController {

    private JsonHandler jsonHandler;
    private JsonDAO jsonDAO;
    private ModelObjectFinder finder;
    private CollectionParser parser;
    private PersonAndRecordParser recordParser;
    private OutputBuilder builder;
    private GetService getService;

    @Autowired
    public GetMappingController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder, CollectionParser parser,
                                PersonAndRecordParser recordParser, OutputBuilder builder, GetService getService) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.parser = parser;
        this.recordParser = recordParser;
        this.builder = builder;
        this.getService = getService;
    }

    private SafetyAlertsModel loadModelFromDisk() {
        try {
            //TODO
            //Add config file to change prod/dev file names
            return jsonHandler.jsonToModel(jsonDAO.readJsonFromFile("testdata.json"));
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
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, model);
        if (firestations.length == 0) {
            //No mappings found for this Firestation number. Return 'not found'.
            return ResponseEntity.notFound().build();
        }
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
        builder.reset();
        for (Person person : peopleAtAddress) {
            builder.addPhone(person.getPhone());
        }
        String responseString = builder.getPhoneNumbersForStationResult();
        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;

    }

    @GetMapping("/fire")
    public ResponseEntity<String> getFirestationNumberAndResidentsForAddress(@RequestParam("address") String address) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request

        Firestation firestation = finder.findFirestation(address, model);
        if (firestation == null) {
            //No mappings found for this address Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);

        builder.reset();
        builder.addFirestation(firestation);

        for (Person person : peopleAtAddress) {
            builder.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
        }
        String responseString = builder.getFirestationNumberAndResidentsForAddressResult(recordParser);

        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<String> getHouseholdsByFirestation(@RequestParam("stations") int[] stationNumbers) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        //Get firestation map objects for all provided station numbers
        builder.reset();

        ArrayList<Firestation> firestationsMappings = new ArrayList<Firestation>();
        for (int stationNumber : stationNumbers) {
            Firestation[] foundStationMappings = finder.findFirestationByNumber(stationNumber, model);
            for (Firestation firestation : foundStationMappings) {
                firestationsMappings.add(firestation);
            }
        }
        if (firestationsMappings.size() == 0) {
            //No mappings found for the provided station numbers. Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        //Loop through firestation mappings and get people, create households and add them to builder
        builder.setStationNumbers(stationNumbers);
        for (Firestation firestation : firestationsMappings) {
            String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
            for (String address : addresses) {
                Household household = new Household(address, firestation.getStation());
                Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
                for (Person person : peopleAtAddress) {
                    household.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
                }
                builder.addHousehold(household);
            }

        }

        String responseString = builder.getHouseholdsByFirestationResult(recordParser);


        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }

    @GetMapping("/personInfo")
    public ResponseEntity<String> getPersonInfoByFirstNameLastName(@RequestParam("firstName") String firstName,
                                                                   @RequestParam("lastName") String lastName) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        Person[] persons = finder.findPersons(firstName, lastName, model);
        builder.reset();

        for (Person person : persons) {
            builder.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
        }

        String responseString = builder.getPersonInfoByFirstNameLastNameResult(new PersonAndRecordParser());

        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<String> getEmailAddressesByCity(@RequestParam("city") String city) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        Person[] persons = finder.findPersonByCity(city, model);
        builder.reset();

        for (Person person : persons) {
            builder.addPerson(person, new MedicalRecord());
        }

        String responseString = builder.getEmailAddressesByCityResult();

        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }

}