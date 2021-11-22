package com.safetynet.alerts.presentation.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.updaters.ResultModel;
import com.safetynet.alerts.logic.updaters.UpdatePerson;
import com.safetynet.alerts.logic.JsonHandler;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RestController for /person endpoint
 *
 * Includes POST/PUT/DELETE
 */
@RestController
public class PersonController {

    private LogHandlerTiny logHandler;
    private JsonHandler jsonHandler;
    private JsonDAO jsonDAO;
    private ModelObjectFinder finder;
    private UpdatePerson updatePerson;
    private DataConfig dataConfig;

    @Autowired
    public PersonController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder,
                            UpdatePerson updatePerson, DataConfig dataConfig, LogHandlerTiny logHandler) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.updatePerson = updatePerson;
        this.dataConfig = dataConfig;
        this.logHandler = logHandler;
    }

    private SafetyAlertsModel loadModelFromDisk() {
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

    /**
     * Mapping for POST
     *
     * Returns:
     * HttpStatus.CONFLICT if medical record already exists
     * HttpStatus.INTERNAL_SERVER_ERROR if data file cannot be accessed
     * Json string & HttpStatus.CREATED if successful
     *
     * @param firstName
     * @param lastName
     * @param address
     * @param city
     * @param zip
     * @param phone
     * @param email
     * @return Json string & HttpStatus.CREATED if successful
     */
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
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("POST", response);
            return response;
        }
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

    /**
     * Mapping for PUT
     *
     * Returns:
     * HttpStatus.NOT_FOUND if Person does not exist
     * HttpStatus.INTERNAL_SERVER_ERROR if data file cannot be accessed
     * HttpStatus.BAD_REQUEST if update fails
     * HttpStatus.OK if successful
     *
     * @param firstName
     * @param lastName
     * @param address
     * @param city
     * @param zip
     * @param phone
     * @param email
     * @return HttpStatus.OK if successful
     */
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
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("PUT", response);
            return response;
        }
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

    /**
     * Mapping for DELETE
     *
     * Returns:
     * HttpStatus.NOT_FOUND if medical record does not exist
     * HttpStatus.INTERNAL_SERVER_ERROR if data file cannot be accessed
     * HttpStatus.BAD_REQUEST if delete fails
     * HttpStatus.OK if successful
     *
     * @param firstName
     * @param lastName
     * @return HttpStatus.OK if successful
     */
    @DeleteMapping("/person")
    public ResponseEntity<String> deleteEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {

        //Log reqquest
        logHandler.setLogger("PersonController");
        logHandler.logRequest("DELETE","/person", new String[] {firstName, lastName});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("DELETE", response);
            return response;
        }
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
