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

    @GetMapping("/childAlert")
    public ResponseEntity<String> getChildrenAtAddress(@RequestParam("Address") String address) {
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

}