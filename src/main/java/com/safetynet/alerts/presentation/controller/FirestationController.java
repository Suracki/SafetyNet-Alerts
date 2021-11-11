package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.logic.ResultModel;
import com.safetynet.alerts.logic.UpdateFirestation;
import com.safetynet.alerts.logic.UpdatePerson;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.JsonHandler;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FirestationController {

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

    @PostMapping("/firestation")
    public ResponseEntity<String> addEntity(@RequestParam("Address") String address, @RequestParam("Station") int station){
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        ModelObjectFinder finder = new ModelObjectFinder();
        Firestation newFireStation;
        if (finder.findFirestation(address, model) == null){
            //Address does not already have a firestation mapped, we can add this mapping
            newFireStation = new Firestation(address,station);
            model.addFirestation(newFireStation);
        }
        else {
            //Address already has a firestation mapped, cannot add
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        //save data
        saveModelToDisk(model);
        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(newFireStation.toString(), responseHeaders, HttpStatus.CREATED);
        return response;
    }

    @PutMapping("/firestation")
    public ResponseEntity<String> updateEntity(@RequestParam("Address") String address, @RequestParam("Station") int station){
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        ModelObjectFinder finder = new ModelObjectFinder();
        UpdateFirestation updateFirestation = new UpdateFirestation();
        Firestation newFireStation;
        if (finder.findFirestation(address, model) == null){
            //Address does not already have a firestation mapped, we cannot update this mapping
            return ResponseEntity.notFound().build();
        }
        else {
            //Address already has a firestation mapped, we can update
            newFireStation = new Firestation(address,station);
            ResultModel result = updateFirestation.updateFirestation(finder, model, newFireStation);
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

    @DeleteMapping("/firestation")
    public ResponseEntity<String> deleteEntity(@RequestParam("Address") String address, @RequestParam("Station") int station){

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        ModelObjectFinder finder = new ModelObjectFinder();
        Firestation newFirestation;
        UpdateFirestation updateFirestation = new UpdateFirestation();
        if (finder.findFirestation(address, model) == null){
            //Firestation mapping is not already in model, we cannot delete them
            return ResponseEntity.notFound().build();
        }
        else {
            //Firestation mapping does exist for this address, we can delete them
            newFirestation = new Firestation(address,station);
            ResultModel result = updateFirestation.deleteFirestation(finder, model, newFirestation);
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
