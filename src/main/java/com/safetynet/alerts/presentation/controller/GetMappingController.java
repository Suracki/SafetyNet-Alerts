package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logic.CollectionParser;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.logic.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class GetMappingController {

    private SafetyAlertsModel loadModelFromDisk() {
        JsonHandler jsonHandler = new JsonHandler();
        JsonDAO jsonDAO = new JsonDAO();
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

    private void saveModelToDisk(SafetyAlertsModel model) {
        JsonHandler jsonHandler = new JsonHandler();
        JsonDAO jsonDAO = new JsonDAO();
        try {
            //TODO
            //Add config file to change prod/dev file names
            jsonDAO.writeJsonToFile("testdata.json",jsonHandler.modelToJson(model));
        }
        catch (Exception e) {
            System.out.println("Error loading database: " + e);
        }
    }

    @GetMapping("/firestation")
    public ResponseEntity<String> getPeopleServicedByStation(@RequestParam("stationNumber") int stationNumber) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        ModelObjectFinder finder = new ModelObjectFinder();
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, model);
        if (firestations.length == 0) {
            //No mappings found for this Firestation number. Return 'not found'.
            return ResponseEntity.notFound().build();
        }
        CollectionParser parser = new CollectionParser();
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
        PersonAndRecordParser recordParser = new PersonAndRecordParser();
        OutputBuilder builder = new OutputBuilder();
        for (Person person : peopleAtAddress) {
            builder.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
            if (recordParser.isAChild(person, model.getMedicalRecords())) {
                builder.addChild();
            }
            else {
                builder.addAdult();
            }
        }
        String responseString = builder.getPeopleServicedByStationResult();
        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;

    }

    @GetMapping("/childAlert")
    public ResponseEntity<String> getChildrenAtAddress(@RequestParam("address") String address) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        ModelObjectFinder finder = new ModelObjectFinder();
        Person[] peopleAtAddress = finder.findPersonByAddress(new String[] {address},model);
        PersonAndRecordParser recordParser = new PersonAndRecordParser();
        OutputBuilder builder = new OutputBuilder();
        for (Person person : peopleAtAddress) {
            if (recordParser.isAChild(person, model.getMedicalRecords())) {
                builder.addChildPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
            }
            else {
                builder.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
            }
        }
        String responseString = builder.getChildrenAtAddressResult(recordParser);


        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<String> getPhoneNumbersForPeopleServicedByStation(@RequestParam("firestation") int stationNumber) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();

        //Perform Request
        ModelObjectFinder finder = new ModelObjectFinder();
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, model);
        if (firestations.length == 0) {
            //No mappings found for this Firestation number. Return 'not found'.
            return ResponseEntity.notFound().build();
        }
        CollectionParser parser = new CollectionParser();
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
        OutputBuilder builder = new OutputBuilder();
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
        ModelObjectFinder finder = new ModelObjectFinder();

        Firestation firestation = finder.findFirestation(address, model);
        if (firestation == null) {
            //No mappings found for this address Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        CollectionParser parser = new CollectionParser();
        String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);

        PersonAndRecordParser recordParser = new PersonAndRecordParser();
        OutputBuilder builder = new OutputBuilder();
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
        ModelObjectFinder finder = new ModelObjectFinder();

        //Get firestation map objects for all provided station numbers
        OutputBuilder builder = new OutputBuilder();

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
        CollectionParser parser = new CollectionParser();
        PersonAndRecordParser recordParser = new PersonAndRecordParser();
        builder.setStationNumbers(stationNumbers);
        for (Firestation firestation : firestationsMappings) {
            String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
            builder.addFirestationCount();
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
        ModelObjectFinder finder = new ModelObjectFinder();
        Person[] persons = finder.findPersons(firstName, lastName, model);
        OutputBuilder builder = new OutputBuilder();

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
        ModelObjectFinder finder = new ModelObjectFinder();
        Person[] persons = finder.findPersonByCity(city, model);
        OutputBuilder builder = new OutputBuilder();

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