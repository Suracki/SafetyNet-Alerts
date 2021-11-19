package com.safetynet.alerts.presentation.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
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

    private LogHandlerTiny logHandler;
    private JsonHandler jsonHandler;
    private JsonDAO jsonDAO;
    private ModelObjectFinder finder;
    private UpdateMedicalRecord updateMedicalRecord;
    private DataConfig dataConfig;

    @Autowired
    public MedicalRecordController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder,
                                   UpdateMedicalRecord updateMedicalRecord, DataConfig dataConfig,
                                   LogHandlerTiny logHandler) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.updateMedicalRecord = updateMedicalRecord;
        this.dataConfig = dataConfig;
        this.logHandler = logHandler;
    }

    private SafetyAlertsModel loadModelFromDisk() {
        try {
            return jsonHandler.jsonToModel(jsonDAO.readJsonFromFile(dataConfig.getDataFile()));
        }
        catch (Exception e) {
            logHandler.setLogger("MedicalRecordController");
            logHandler.error("Error loading database file " + e);
        }
        return null;
    }

    private void saveModelToDisk(SafetyAlertsModel model) {
        try {
            jsonDAO.writeJsonToFile(dataConfig.getDataFile(),jsonHandler.modelToJson(model));
        }
        catch (Exception e) {
            logHandler.setLogger("MedicalRecordController");
            logHandler.error("Error saving database file " + e);
        }
    }

    private String stringArrayToString(String[] stringArray) {
        StringBuilder builder = new StringBuilder();
        if (stringArray.length == 0) {
            return "[]";
        }
        builder.append("[");
        for (String string : stringArray) {
            builder.append("\"")
                    .append(string)
                    .append("\",");
        }
        //remove final ,
        builder.setLength(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

    @PostMapping("/medicalRecord")
    public ResponseEntity<String> addEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                            @RequestParam("birthDate") String birthdate, @RequestParam("medications") String[] medications,
                                            @RequestParam("allergies") String[] allergies) {
        //Log reqquest
        logHandler.setLogger("MedicalRecordController");
        logHandler.logRequest("POST","/medicalRecord",
                new String[] {firstName, lastName, birthdate, stringArrayToString(medications), stringArrayToString(allergies)});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("POST", response);
            return response;
        }
        //Perform Request
        MedicalRecord newMedicalRecord;
        if (finder.findMedicalRecord(firstName, lastName, model) == null){
            //Medical Record is not already in model, we can add them
            newMedicalRecord = new MedicalRecord(firstName,lastName,birthdate,medications,allergies);
            model.addMedicalRecord(newMedicalRecord);
        }
        else {
            //Medical record already exists with this firstName/lastName combination, call fails
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logHandler.logResponse("POST",response);
            return response;
        }

        //save data
        saveModelToDisk(model);
        //respond
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newMedicalRecord);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);

        //Log response
        logHandler.logResponse("POST", response);
        return response;
    }

    @PutMapping("/medicalRecord")
    public ResponseEntity<String> updateEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                               @RequestParam("birthDate") String birthdate, @RequestParam("medications") String[] medications,
                                               @RequestParam("allergies") String[] allergies) {
        //Log reqquest
        logHandler.setLogger("MedicalRecordController");
        logHandler.logRequest("PUT","/medicalRecord",
                new String[] {firstName, lastName, birthdate, stringArrayToString(medications), stringArrayToString(allergies)});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("PUT", response);
            return response;
        }
        //Perform Request
        MedicalRecord newMedicalRecord;
        if (finder.findMedicalRecord(firstName, lastName, model) == null){
            //Medical record is not already in model, we cannot update them
            ResponseEntity<String> response = ResponseEntity.notFound().build();
            logHandler.logResponse("PUT",response);
            return response;
        }
        else {
            //Person already exists with this firstName/lastName combination, call fails
            newMedicalRecord = new MedicalRecord(firstName,lastName,birthdate,medications,allergies);
            ResultModel result = updateMedicalRecord.updateMedicalRecord(finder, model, newMedicalRecord);
            if (result.getBool()) {
                //Recprd was added successfully
                model = result.getModel();
            }
            else {
                //Record failed to be added
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("PUT",response);
                return response;
            }
        }

        //save data
        saveModelToDisk(model);
        //respond
        ResponseEntity<String> response = ResponseEntity.ok().build();

        //Log response
        logHandler.logResponse("PUT", response);
        return response;
    }

    @DeleteMapping("/medicalRecord")
    public ResponseEntity<String> deleteEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        //Log reqquest
        logHandler.setLogger("MedicalRecordController");
        logHandler.logRequest("DELETE","/medicalRecord", new String[] {firstName, lastName});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("DELETE", response);
            return response;
        }
        //Perform Request
        MedicalRecord newMedicalRecord;
        if (finder.findMedicalRecord(firstName, lastName, model) == null){
            //Medical Record is not already in model, we cannot delete them
            ResponseEntity<String> response = ResponseEntity.notFound().build();
            logHandler.logResponse("DELETE",response);
            return response;
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
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("DELETE",response);
                return response;
            }
        }

        //save data
        saveModelToDisk(model);
        //respond
        ResponseEntity<String> response = ResponseEntity.ok().build();

        //Log response
        logHandler.logResponse("DELETE", response);
        return response;
    }

}
