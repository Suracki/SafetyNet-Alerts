package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logic.CollectionParser;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.logic.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.JsonHandler;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/person")
    public ResponseEntity<String> getEntity(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName,
                                            @RequestParam("Address") String address, @RequestParam("City") String city,
                                            @RequestParam("Zip") String zip, @RequestParam("Phone") String phone,
                                            @RequestParam("EMail") String email) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        ModelObjectFinder finder = new ModelObjectFinder();
        Person newPerson;
        if (finder.findPerson(firstName, lastName, model) == null){
            //Person is not already in model, we can add them
            newPerson = new Person(firstName,lastName,address,city,zip,phone,email);
            model.addPerson(newPerson);
        }
        else {
            //Person already exists with this firstName/lastName combination, call fails
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        //save data
        saveModelToDisk(model);
        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(newPerson.toString(), responseHeaders, HttpStatus.CREATED);
        return response;
    }

}