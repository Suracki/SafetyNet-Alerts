package com.safetynet.alerts.logic.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.logic.updaters.ResultModel;
import com.safetynet.alerts.logic.updaters.UpdateFirestation;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FirestationService extends BaseService {

    @Autowired
    private UpdateFirestation updateFirestation;

    public ResponseEntity<String> addEntityService(String address, int station) {

        Firestation newFireStation;
        if (finder.findFirestation(address, safetyAlertsModel) == null){
            //Address does not already have a firestation mapped, we can add this mapping
            newFireStation = new Firestation(address,station);
            safetyAlertsModel.addFirestation(newFireStation);
        }
        else {
            //Address already has a firestation mapped, cannot add
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logHandler.logResponse("POST",response);
            return response;
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newFireStation);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);

        return response;
    }

    public ResponseEntity<String> updateEntityService(String address, int station) {
        Firestation newFireStation;
        if (finder.findFirestation(address, safetyAlertsModel) == null){
            //Address does not already have a firestation mapped, we cannot update this mapping
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logHandler.logResponse("PUT", response);
            return response;
        }
        else {
            //Address already has a firestation mapped, we can update
            newFireStation = new Firestation(address,station);
            ResultModel result = updateFirestation.updateFirestation(finder, safetyAlertsModel, newFireStation);
            if (result.successful()) {
                //Mapping was added successfully
                safetyAlertsModel.updateModel(result.getModel());
            }
            else {
                //Mapping failed to be added
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("PUT",response);
                return response;
            }
        }

        return ResponseEntity.ok().build();

    }

    public ResponseEntity<String> deleteEntityService(String address, int station) {

        Firestation newFirestation;
        if (finder.findFirestation(address, safetyAlertsModel) == null){
            //Firestation mapping is not already in model, we cannot delete them
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logHandler.logResponse("DELETE", response);
            return response;
        }
        else {
            //Firestation mapping does exist for this address, we can delete them
            newFirestation = new Firestation(address,station);
            ResultModel result = updateFirestation.deleteFirestation(finder, safetyAlertsModel, newFirestation);
            if (result.successful()) {
                //Mapping was deleted successfully
                safetyAlertsModel.updateModel(result.getModel());
            }
            else {
                //Mapping failed to be deleted
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("DELETE",response);
                return response;
            }
        }

        return ResponseEntity.ok().build();
    }

}
