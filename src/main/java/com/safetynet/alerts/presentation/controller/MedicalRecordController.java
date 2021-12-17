package com.safetynet.alerts.presentation.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.service.MedicalRecordService;
import com.safetynet.alerts.logic.updaters.ResultModel;
import com.safetynet.alerts.logic.updaters.UpdateMedicalRecord;
import com.safetynet.alerts.logic.JsonHandler;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RestController for /medicalRecord endpoint
 *
 * Includes POST/PUT/DELETE
 */
@RestController
public class MedicalRecordController extends BaseController {

    @Autowired
    private MedicalRecordService medicalRecordService;

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
     * @param birthdate
     * @param medications
     * @param allergies
     * @return Json string & HttpStatus.CREATED if successful
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<String> addEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                            @RequestParam("birthDate") String birthdate, @RequestParam("medications") String[] medications,
                                            @RequestParam("allergies") String[] allergies) {
        //Log reqquest
        logHandler.setLogger("MedicalRecordController");
        logHandler.logRequest("POST","/medicalRecord",
                new String[] {firstName, lastName, birthdate, stringArrayToString(medications), stringArrayToString(allergies)});
        //confirm data is loaded
        if (!safetyAlertsModel.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("POST", response);
            return response;
        }
        //Perform Request
        ResponseEntity<String> response = medicalRecordService.addEntityService(safetyAlertsModel, firstName, lastName,
                birthdate, medications, allergies);

        //Log response
        logHandler.logResponse("POST", response);
        //respond
        return response;
    }

    /**
     * Mapping for PUT
     *
     * Returns:
     * HttpStatus.NOT_FOUND if medicalRecord does not exist
     * HttpStatus.INTERNAL_SERVER_ERROR if data file cannot be accessed
     * HttpStatus.BAD_REQUEST if update fails
     * HttpStatus.OK if successful
     *
     * @param firstName
     * @param lastName
     * @param birthdate
     * @param medications
     * @param allergies
     * @return HttpStatus.OK if successful
     */
    @PutMapping("/medicalRecord")
    public ResponseEntity<String> updateEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                               @RequestParam("birthDate") String birthdate, @RequestParam("medications") String[] medications,
                                               @RequestParam("allergies") String[] allergies) {
        //Log reqquest
        logHandler.setLogger("MedicalRecordController");
        logHandler.logRequest("PUT","/medicalRecord",
                new String[] {firstName, lastName, birthdate, stringArrayToString(medications), stringArrayToString(allergies)});
        //confirm data is loaded
        if (!safetyAlertsModel.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("PUT", response);
            return response;
        }
        //Perform Request
        ResponseEntity<String> response = medicalRecordService.updateEntityService(safetyAlertsModel, firstName, lastName,
                birthdate, medications, allergies);

        //Log response
        logHandler.logResponse("PUT", response);
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
    @DeleteMapping("/medicalRecord")
    public ResponseEntity<String> deleteEntity(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        //Log request
        logHandler.setLogger("MedicalRecordController");
        logHandler.logRequest("DELETE","/medicalRecord", new String[] {firstName, lastName});

        //confirm data is loaded
        if (!safetyAlertsModel.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("DELETE", response);
            return response;
        }
        //Perform Request
        ResponseEntity<String> response = medicalRecordService.deleteEntityService(safetyAlertsModel, firstName, lastName);

        //Log response
        logHandler.logResponse("DELETE", response);
        //respond
        return response;
    }

}
