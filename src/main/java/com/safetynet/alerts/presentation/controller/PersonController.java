package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.presentation.model.JsonHandler;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PersonController {

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

    @GetMapping("/personPhone")
    public String getPhoneNumberByName(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName){
        SafetyAlertsModel model = loadModelFromDisk();

        ModelObjectFinder finder = new ModelObjectFinder();

        String phoneNumber = finder.findPerson(firstName, lastName, model).getPhone();
        return firstName + " " + lastName + ": " + phoneNumber;
    }

    @GetMapping("/personInfo")
    public Map<Person, MedicalRecord> getPersonInfo(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName){

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform our request
        ModelObjectFinder finder = new ModelObjectFinder();
        Person person = finder.findPerson(firstName, lastName, model);
        MedicalRecord record = finder.findRecord(firstName, lastName, model);

        Map<Person, MedicalRecord> result = new HashMap<Person, MedicalRecord>();
        result.put(person,record);

        //save data
        //saveModelToDisk(model); operation makes no changes; we don't save.
        return result;
    }
}
