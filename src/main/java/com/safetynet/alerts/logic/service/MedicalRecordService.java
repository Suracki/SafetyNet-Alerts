package com.safetynet.alerts.logic.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.updaters.ResultModel;
import com.safetynet.alerts.logic.updaters.UpdateMedicalRecord;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordService {

    @Autowired
    private LogHandlerTiny logHandler;
    @Autowired
    private ModelObjectFinder finder;
    @Autowired
    private UpdateMedicalRecord updateMedicalRecord;

    public ResponseEntity<String> addEntityService(SafetyAlertsModel safetyAlertsModel, String firstName, String lastName,
                                                   String birthdate, String[] medications, String[] allergies){
        MedicalRecord newMedicalRecord;
        if (finder.findMedicalRecord(firstName, lastName, safetyAlertsModel) == null){
            //Medical Record is not already in model, we can add them
            newMedicalRecord = new MedicalRecord(firstName,lastName,birthdate,medications,allergies);
            safetyAlertsModel.addMedicalRecord(newMedicalRecord);
        }
        else {
            //Medical record already exists with this firstName/lastName combination, call fails
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logHandler.logResponse("POST",response);
            return response;
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newMedicalRecord);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);
        return response;
    }

    public ResponseEntity<String> updateEntityService(SafetyAlertsModel safetyAlertsModel, String firstName, String lastName,
                                                   String birthdate, String[] medications, String[] allergies){

        MedicalRecord newMedicalRecord;
        if (finder.findMedicalRecord(firstName, lastName, safetyAlertsModel) == null){
            //Medical record is not already in model, we cannot update them
            ResponseEntity<String> response = ResponseEntity.notFound().build();
            logHandler.logResponse("PUT",response);
            return response;
        }
        else {
            //Record already exists with this firstName/lastName combination, we can update
            newMedicalRecord = new MedicalRecord(firstName,lastName,birthdate,medications,allergies);
            ResultModel result = updateMedicalRecord.updateMedicalRecord(finder, safetyAlertsModel, newMedicalRecord);
            if (result.getBool()) {
                //Record was updated successfully
                safetyAlertsModel.updateModel(result.getModel());
            }
            else {
                //Record failed to be updated
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("PUT",response);
                return response;
            }
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> deleteEntityService(SafetyAlertsModel safetyAlertsModel, String firstName, String lastName) {

        MedicalRecord newMedicalRecord;
        if (finder.findMedicalRecord(firstName, lastName, safetyAlertsModel) == null){
            //Medical Record is not already in model, we cannot delete them
            ResponseEntity<String> response = ResponseEntity.notFound().build();
            logHandler.logResponse("DELETE",response);
            return response;
        }
        else {
            //Medical Record does exist, we can delete it
            newMedicalRecord = new MedicalRecord(firstName,lastName,null,new String[0],new String[0]);
            ResultModel result = updateMedicalRecord.deleteMedicalRecord(finder, safetyAlertsModel, newMedicalRecord);
            if (result.getBool()) {
                //Medical Record was deleted successfully
                safetyAlertsModel.updateModel(result.getModel());
            }
            else {
                //Medical Record failed to be deleted
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("DELETE",response);
                return response;
            }
        }

        return ResponseEntity.ok().build();
    }
}
