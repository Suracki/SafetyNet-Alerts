package com.safetynet.alerts.presentation.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandler;
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

import javax.xml.crypto.Data;


@RestController
public class PersonController {

    private LogHandler logHandler;
    private JsonHandler jsonHandler;
    private JsonDAO jsonDAO;
    private ModelObjectFinder finder;
    private UpdatePerson updatePerson;
    private DataConfig dataConfig;

    @Autowired
    public PersonController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder,
                            UpdatePerson updatePerson, DataConfig dataConfig, LogHandler logHandler) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.updatePerson = updatePerson;
        this.dataConfig = dataConfig;
        this.logHandler = logHandler;
    }

    private SafetyAlertsModel loadModelFromDisk() {
        JsonHandler jsonHandler = new JsonHandler();
        JsonDAO jsonDAO = new JsonDAO();
        try {
            return jsonHandler.jsonToModel(jsonDAO.readJsonFromFile(dataConfig.getDataFile()));
        }
        catch (Exception e) {
            logHandler.setLogger("PersonController");
            logHandler.error("Error loading database file " + e);
        }
        return null;
    }

    private void saveModelToDisk(SafetyAlertsModel model) {
        JsonHandler jsonHandler = new JsonHandler();
        JsonDAO jsonDAO = new JsonDAO();
        try {
            jsonDAO.writeJsonToFile(dataConfig.getDataFile(),jsonHandler.modelToJson(model));
        }
        catch (Exception e) {
            logHandler.setLogger("PersonController");
            logHandler.error("Error saving database file " + e);
        }
    }

    @PostMapping("/person")
    public ResponseEntity<String> addEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                            @RequestParam("address") String address, @RequestParam("city") String city,
                                            @RequestParam("zip") String zip, @RequestParam("phone") String phone,
                                            @RequestParam("email") String email) {
        //Log reqquest
        logHandler.setLogger("PersonController");
        logHandler.logRequest("POST","/person", new String[] {firstName, lastName, address, city, zip, phone, email});

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
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logHandler.logResponse("POST",response);
            return response;
        }

        //save data
        saveModelToDisk(model);
        //respond
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newPerson);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);

        //Log response
        logHandler.logResponse("POST", response);
        return response;
    }

    @PutMapping("/person")
    public ResponseEntity<String> updateEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                               @RequestParam("address") String address, @RequestParam("city") String city,
                                               @RequestParam("zip") String zip, @RequestParam("phone") String phone,
                                               @RequestParam("email") String email) {
        //Log reqquest
        logHandler.setLogger("PersonController");
        logHandler.logRequest("PUT","/person", new String[] {firstName, lastName, address, city, zip, phone, email});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        Person newPerson;
        if (finder.findPerson(firstName, lastName, model) == null){
            //Person is not already in model, we cannot update them
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logHandler.logResponse("PUT", response);
            return response;
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
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("PUT",response);
                return response;
            }
        }

        //save data
        saveModelToDisk(model);
        //Log response
        ResponseEntity<String> response = ResponseEntity.ok().build();
        logHandler.logResponse("PUT",response);
        //respond
        return response;
    }

    @DeleteMapping("/person")
    public ResponseEntity<String> deleteEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {

        //Log reqquest
        logHandler.setLogger("PersonController");
        logHandler.logRequest("DELETE","/person", new String[] {firstName, lastName});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        Person newPerson;
        if (finder.findPerson(firstName, lastName, model) == null){
            //Person is not already in model, we cannot delete them
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logHandler.logResponse("DELETE",response);
            return response;
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
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("DELETE",response);
                return response;
            }
        }

        //save data
        saveModelToDisk(model);
        //Log response
        ResponseEntity<String> response = ResponseEntity.ok().build();
        logHandler.logResponse("DELETE",response);
        //respond
        return response;
    }

}
