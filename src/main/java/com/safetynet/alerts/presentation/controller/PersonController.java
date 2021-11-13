package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.logic.ResultModel;
import com.safetynet.alerts.logic.UpdatePerson;
import com.safetynet.alerts.presentation.model.JsonHandler;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class PersonController {

    JsonHandler jsonHandler;
    JsonDAO jsonDAO;
    ModelObjectFinder finder;
    UpdatePerson updatePerson;

    @Autowired
    public PersonController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder, UpdatePerson updatePerson) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.updatePerson = updatePerson;
    }

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

    @PostMapping("/person")
    public ResponseEntity<String> addEntity(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName,
                                            @RequestParam("Address") String address, @RequestParam("City") String city,
                                            @RequestParam("Zip") String zip, @RequestParam("Phone") String phone,
                                            @RequestParam("EMail") String email) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
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

    @PutMapping("/person")
    public ResponseEntity<String> updateEntity(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName,
                                               @RequestParam("Address") String address, @RequestParam("City") String city,
                                               @RequestParam("Zip") String zip, @RequestParam("Phone") String phone,
                                               @RequestParam("EMail") String email) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        Person newPerson;
        if (finder.findPerson(firstName, lastName, model) == null){
            //Person is not already in model, we cannot update them
            return ResponseEntity.notFound().build();
        }
        else {
            //Person already exists with this firstName/lastName combination, call fails
            newPerson = new Person(firstName,lastName,address,city,zip,phone,email);
            ResultModel result = updatePerson.updatePerson(finder, model, newPerson);
            if (result.getBool()) {
                //Person was added successfully
                model = result.getModel();
            }
            else {
                //Person failed to be added
                //TODO add specific error details once we check fields
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        //save data
        saveModelToDisk(model);
        //respond
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/person")
    public ResponseEntity<String> deleteEntity(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName) {

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        Person newPerson;
        if (finder.findPerson(firstName, lastName, model) == null){
            //Person is not already in model, we cannot delete them
            return ResponseEntity.notFound().build();
        }
        else {
            //Person does exist, we can delete them
            newPerson = new Person(firstName,lastName,"","","","","");
            ResultModel result = updatePerson.deletePerson(finder, model, newPerson);
            if (result.getBool()) {
                //Person was deleted successfully
                model = result.getModel();
            }
            else {
                //Person failed to be deleted
                //TODO add specific error details once we check fields
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        //save data
        saveModelToDisk(model);
        //respond
        return ResponseEntity.ok().build();
    }

}
