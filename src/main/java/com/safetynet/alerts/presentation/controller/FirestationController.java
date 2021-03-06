package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.service.FirestationService;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RestController for /firestation endpoint
 *
 * Includes POST/PUT/DELETE
 */
@RestController
public class FirestationController extends BaseController{

    @Autowired
    private FirestationService firestationService;


    /**
     * Mapping for POST
     *
     * Returns:
     * HttpStatus.CONFLICT if mapping already exists
     * HttpStatus.INTERNAL_SERVER_ERROR if data file cannot be accessed
     * Json string & HttpStatus.CREATED if successful
     *
     * @param address address for Firestation mapping
     * @param station station number for Firestation mapping
     * @return Json string & HttpStatus.CREATED if successful
     */
    @PostMapping("/firestation")
    public ResponseEntity<String> addEntity(@RequestParam("address") String address, @RequestParam("station") int station){
        //Log reqquest
        logHandler.setLogger("FirestationController");
        logHandler.logRequest("POST","/firestation", new String[] {address, String.valueOf(station)});

        //confirm data is loaded
        if (!firestationService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("POST", response);
            return response;
        }
        //Perform Request
        ResponseEntity<String> response = firestationService.addEntityService(address, station);

        //Log response
        logHandler.logResponse("POST", response);

        //respond
        return response;
    }

    /**
     * Mapping for PUT
     *
     * Returns:
     * HttpStatus.NOT_FOUND if mapping does not exist
     * HttpStatus.INTERNAL_SERVER_ERROR if data file cannot be accessed
     * HttpStatus.BAD_REQUEST if update fails
     * HttpStatus.OK if successful
     *
     * @param address address for Firestation mapping
     * @param station station number for Firestation mapping
     * @return HttpStatus.OK if successful
     */
    @PutMapping("/firestation")
    public ResponseEntity<String> updateEntity(@RequestParam("address") String address, @RequestParam("station") int station){
        //Log reqquest
        logHandler.setLogger("FirestationController");
        logHandler.logRequest("PUT","/firestation", new String[] {address, String.valueOf(station)});

        //confirm data is loaded
        if (!firestationService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("PUT", response);
            return response;
        }
        //Perform Request
        ResponseEntity<String> response = firestationService.updateEntityService(address, station);

        //Log response
        logHandler.logResponse("PUT",response);
        //respond
        return response;
    }

    /**
     * Mapping for DELETE
     *
     * Returns:
     * HttpStatus.NOT_FOUND if mapping does not exist
     * HttpStatus.INTERNAL_SERVER_ERROR if data file cannot be accessed
     * HttpStatus.BAD_REQUEST if delete fails
     * HttpStatus.OK if successful
     *
     * @param address address for Firestation mapping
     * @param station station number for Firestation mapping
     * @return HttpStatus.OK if successful
     */
    @DeleteMapping("/firestation")
    public ResponseEntity<String> deleteEntity(@RequestParam("address") String address, @RequestParam("station") int station){
        //Log reqquest
        logHandler.setLogger("FirestationController");
        logHandler.logRequest("DELETE","/firestation", new String[] {address, String.valueOf(station)});

        //confirm data is loaded
        if (!firestationService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("DELETE", response);
            return response;
        }
        //Perform Request
        ResponseEntity<String> response = firestationService.deleteEntityService(address, station);

        //Log response
        logHandler.logResponse("DELETE",response);
        //respond
        return response;
    }

}
