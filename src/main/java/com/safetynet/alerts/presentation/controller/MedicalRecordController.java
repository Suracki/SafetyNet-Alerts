package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.logic.ResultModel;
import com.safetynet.alerts.logic.UpdateMedicalRecord;
import com.safetynet.alerts.presentation.model.JsonHandler;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MedicalRecordController {

    JsonHandler jsonHandler;
    JsonDAO jsonDAO;
    ModelObjectFinder finder;
    UpdateMedicalRecord updateMedicalRecord;

    @Autowired
    public MedicalRecordController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder, UpdateMedicalRecord updateMedicalRecord) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.updateMedicalRecord = updateMedicalRecord;
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

    private void saveModelToDisk(SafetyAlertsModel model) {
        try {
            //TODO
            //Add config file to change prod/dev file names
            jsonDAO.writeJsonToFile("testdata.json",jsonHandler.modelToJson(model));
        }
        catch (Exception e) {
            System.out.println("Error loading database: " + e);
        }
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<String> addEntity(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName,
                                            @RequestParam("BirthDate") String birthdate, @RequestParam("Medications") String[] medications,
                                            @RequestParam("Allergies") String[] allergies) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        MedicalRecord newMedicalRecord;
        if (finder.findMedicalRecord(firstName, lastName, model) == null){
            //Medical Record is not already in model, we can add them
            newMedicalRecord = new MedicalRecord(firstName,lastName,birthdate,medications,allergies);
            model.addMedicalRecord(newMedicalRecord);
        }
        else {
            //Medical record already exists with this firstName/lastName combination, call fails
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        //save data
        saveModelToDisk(model);
        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(newMedicalRecord.toString(), responseHeaders, HttpStatus.CREATED);
        return response;
    }

    @PutMapping("/medicalRecord")
    public ResponseEntity<String> updateEntity(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName,
                                               @RequestParam("BirthDate") String birthdate, @RequestParam("Medications") String[] medications,
                                               @RequestParam("Allergies") String[] allergies) {
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        MedicalRecord newMedicalRecord;
        if (finder.findMedicalRecord(firstName, lastName, model) == null){
            //Medical record is not already in model, we cannot update them
            return ResponseEntity.notFound().build();
        }
        else {
            //Person already exists with this firstName/lastName combination, call fails
            newMedicalRecord = new MedicalRecord(firstName,lastName,birthdate,medications,allergies);
            ResultModel result = updateMedicalRecord.updateMedicalRecord(finder, model, newMedicalRecord);
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

    @DeleteMapping("/medicalRecord")
    public ResponseEntity<String> deleteEntity(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName) {

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        MedicalRecord newMedicalRecord;
        if (finder.findMedicalRecord(firstName, lastName, model) == null){
            //Medical Record is not already in model, we cannot delete them
            return ResponseEntity.notFound().build();
        }
        else {
            //Medical Record does exist, we can delete it
            newMedicalRecord = new MedicalRecord(firstName,lastName,null,new String[0],new String[0]);
            ResultModel result = updateMedicalRecord.deleteMedicalRecord(finder, model, newMedicalRecord);
            if (result.getBool()) {
                //Medical Record was deleted successfully
                model = result.getModel();
            }
            else {
                //Medical Record failed to be deleted
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
