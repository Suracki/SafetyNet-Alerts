package com.safetynet.alerts.logic.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.logic.updaters.ResultModel;
import com.safetynet.alerts.logic.updaters.UpdatePerson;
import com.safetynet.alerts.presentation.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class PersonService extends BaseService{

    @Autowired
    private UpdatePerson updatePerson;

    public ResponseEntity<String> addEntityService(String firstName, String lastName,
                                                   String address, String city, String zip, String phone, String email) {

        Person newPerson;
        if (finder.findPerson(firstName, lastName, safetyAlertsModel) == null){
            //Person is not already in model, we can add them
            newPerson = new Person(firstName,lastName,address,city,zip,phone,email);
            safetyAlertsModel.addPerson(newPerson);
        }
        else {
            //Person already exists with this firstName/lastName combination, call fails
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logHandler.logResponse("POST",response);
            return response;
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newPerson);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);

        return response;

    }


    public ResponseEntity<String> updateEntityService(String firstName, String lastName,
                                                      String address, String city, String zip, String phone, String email) {


        Person newPerson;
        if (finder.findPerson(firstName, lastName, safetyAlertsModel) == null){
            //Person is not already in model, we cannot update them
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logHandler.logResponse("PUT", response);
            return response;
        }
        else {
            //Person already exists with this firstName/lastName combination, we can update
            newPerson = new Person(firstName,lastName,address,city,zip,phone,email);
            ResultModel result = updatePerson.updatePerson(finder, safetyAlertsModel, newPerson);
            if (result.successful()) {
                //Person was updated successfully
                safetyAlertsModel.updateModel(result.getModel());
            }
            else {
                //Person failed to be updated
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("PUT",response);
                return response;
            }
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> deleteEntityService(String firstName, String lastName) {
        Person newPerson;
        if (finder.findPerson(firstName, lastName, safetyAlertsModel) == null){
            //Person is not already in model, we cannot delete them
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logHandler.logResponse("DELETE",response);
            return response;
        }
        else {
            //Person does exist, we can delete them
            newPerson = new Person(firstName,lastName,"","","","","");
            ResultModel result = updatePerson.deletePerson(finder, safetyAlertsModel, newPerson);
            if (result.successful()) {
                //Person was deleted successfully
                safetyAlertsModel.updateModel(result.getModel());
            }
            else {
                //Person failed to be deleted
                ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                logHandler.logResponse("DELETE",response);
                return response;
            }
        }
        return ResponseEntity.ok().build();
    }

}
