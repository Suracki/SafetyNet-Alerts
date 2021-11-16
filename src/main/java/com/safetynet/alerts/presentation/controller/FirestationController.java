package com.safetynet.alerts.presentation.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandler;
import com.safetynet.alerts.logic.ModelObjectFinder;
import com.safetynet.alerts.logic.ResultModel;
import com.safetynet.alerts.logic.UpdateFirestation;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.JsonHandler;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FirestationController {

    private LogHandler logHandler;
    private JsonHandler jsonHandler;
    private JsonDAO jsonDAO;
    private ModelObjectFinder finder;
    private UpdateFirestation updateFirestation;
    private DataConfig dataConfig;

    @Autowired
    public FirestationController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder,
                                 UpdateFirestation updateFirestation, DataConfig dataConfig,
                                 LogHandler logHandler) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.updateFirestation = updateFirestation;
        this.dataConfig = dataConfig;
        this.logHandler = logHandler;
    }

    private SafetyAlertsModel loadModelFromDisk() {

        try {
            return jsonHandler.jsonToModel(jsonDAO.readJsonFromFile(dataConfig.getDataFile()));
        }
        catch (Exception e) {
            logHandler.setLogger("FirestationController");
            logHandler.error("Error loading database file " + e);
        }
        return null;
    }

    private void saveModelToDisk(SafetyAlertsModel model) {

        try {
            jsonDAO.writeJsonToFile(dataConfig.getDataFile(),jsonHandler.modelToJson(model));
        }
        catch (Exception e) {
            logHandler.setLogger("FirestationController");
            logHandler.error("Error saving database file " + e);
        }
    }

    @PostMapping("/firestation")
    public ResponseEntity<String> addEntity(@RequestParam("address") String address, @RequestParam("station") int station){
        //Log reqquest
        logHandler.setLogger("FirestationController");
        logHandler.logRequest("POST","/firestation", new String[] {address, String.valueOf(station)});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        Firestation newFireStation;
        if (finder.findFirestation(address, model) == null){
            //Address does not already have a firestation mapped, we can add this mapping
            newFireStation = new Firestation(address,station);
            model.addFirestation(newFireStation);
        }
        else {
            //Address already has a firestation mapped, cannot add
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CONFLICT).build();
            logHandler.logResponse("POST",response);
            return response;
        }

        //save data
        saveModelToDisk(model);
        //respond
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(newFireStation);
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<>(responseString, responseHeaders, HttpStatus.CREATED);

        //Log response
        logHandler.logResponse("POST", response);

        return response;
    }

    @PutMapping("/firestation")
    public ResponseEntity<String> updateEntity(@RequestParam("address") String address, @RequestParam("station") int station){
        //Log reqquest
        logHandler.setLogger("FirestationController");
        logHandler.logRequest("PUT","/firestation", new String[] {address, String.valueOf(station)});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        Firestation newFireStation;
        if (finder.findFirestation(address, model) == null){
            //Address does not already have a firestation mapped, we cannot update this mapping
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logHandler.logResponse("PUT", response);
            return response;
        }
        else {
            //Address already has a firestation mapped, we can update
            newFireStation = new Firestation(address,station);
            ResultModel result = updateFirestation.updateFirestation(finder, model, newFireStation);
            if (result.getBool()) {
                //Mapping was added successfully
                model = result.getModel();
            }
            else {
                //Mapping failed to be added
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

    @DeleteMapping("/firestation")
    public ResponseEntity<String> deleteEntity(@RequestParam("address") String address, @RequestParam("station") int station){
        //Log reqquest
        logHandler.setLogger("FirestationController");
        logHandler.logRequest("DELETE","/firestation", new String[] {address, String.valueOf(station)});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        //Perform Request
        Firestation newFirestation;
        if (finder.findFirestation(address, model) == null){
            //Firestation mapping is not already in model, we cannot delete them
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            logHandler.logResponse("DELETE", response);
            return response;
        }
        else {
            //Firestation mapping does exist for this address, we can delete them
            newFirestation = new Firestation(address,station);
            ResultModel result = updateFirestation.deleteFirestation(finder, model, newFirestation);
            if (result.getBool()) {
                //Mapping was deleted successfully
                model = result.getModel();
            }
            else {
                //Mapping failed to be deleted
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
