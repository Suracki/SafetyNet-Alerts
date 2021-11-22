package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.*;
import com.safetynet.alerts.logic.parsers.CollectionParser;
import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.parsers.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * RestController for custom endpoint GET mappings
 */
@RestController
public class GetMappingController {

    private LogHandlerTiny logHandler;
    private JsonHandler jsonHandler;
    private JsonDAO jsonDAO;
    private ModelObjectFinder finder;
    private CollectionParser parser;
    private PersonAndRecordParser recordParser;
    private GetService getService;
    private DataConfig dataConfig;

    @Autowired
    public GetMappingController(JsonHandler jsonHandler, JsonDAO jsonDAO, ModelObjectFinder finder, CollectionParser parser,
                                PersonAndRecordParser recordParser, GetService getService,
                                DataConfig dataConfig, LogHandlerTiny logHandler) {
        this.jsonHandler = jsonHandler;
        this.jsonDAO = jsonDAO;
        this.finder = finder;
        this.parser = parser;
        this.recordParser = recordParser;
        this.getService = getService;
        this.dataConfig = dataConfig;
        this.logHandler = logHandler;
    }

    private SafetyAlertsModel loadModelFromDisk() {
        try {
            return jsonHandler.jsonToModel(jsonDAO.readJsonFromFile(dataConfig.getDataFile()));
        }
        catch (Exception e) {
            logHandler.setLogger("GetMappingController");
            logHandler.error("Error loading database file " + e);
        }
        return null;
    }

    private String[] intArrayToStringArray(int[] intArray) {
        return Arrays.stream(intArray).mapToObj(String::valueOf).toArray(String[]::new);
    }

    /**
     * Returns all people serviced by a specific fire station.
     * Includes first name, last name, address, and phone number for each person
     * Also includes summary of the number of adults and children in the service area
     *
     * @param stationNumber
     * @return Json object as string in ResponseEntity
     */
    @GetMapping("/firestation")
    public ResponseEntity<String> getPeopleServicedByStation(@RequestParam("stationNumber") int stationNumber) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/firestation", new String[] {String.valueOf(stationNumber)});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform request
        ResponseEntity<String> response = getService.getPeopleServicedByStationEntity(stationNumber, model, finder,
                parser, recordParser, logHandler);
        //Log response
        logHandler.logResponse("GET", response);
        //Respond
        return response;

    }

    /**
     * Returns all children (people under 18) at a specified address.
     * Includes first name, last name, age for each child
     * Also includes separated list of adults living at the address
     * If no children are at the address, response contains an empty string
     *
     * @param address
     * @return Json object as string in ResponseEntity
     */
    @GetMapping("/childAlert")
    public ResponseEntity<String> getChildrenAtAddress(@RequestParam("address") String address) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/childAlert", new String[] {address});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getChildrenAtAddressEntity(address, model, finder, recordParser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);

        //Respond
        return response;
    }

    /**
     * Returns a list of phone numbers for each person within the provided firestation's jurisdiction
     *
     * @param stationNumber
     * @return Json object as string in ResponseEntity
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<String> getPhoneNumbersForPeopleServicedByStation(@RequestParam("firestation") int stationNumber) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/phoneAlert", new String[] {String.valueOf(stationNumber)});

        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getPhoneNumbersForPeopleServicedByStationEntity(stationNumber, model,
                finder, parser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);

        //Respond
        return response;

    }

    /**
     * Returns the firestation number that services a provided address
     * Also includes details of all people living at the address
     *
     * @param address
     * @return Json object as string in ResponseEntity
     */
    @GetMapping("/fire")
    public ResponseEntity<String> getFirestationNumberAndResidentsForAddress(@RequestParam("address") String address) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/fire", new String[] {address});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getFirestationNumberAndResidentsForAddressEntity(address, model, finder,
                parser, recordParser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);
        //respond
        return response;
    }

    /**
     * Returns a list of all households in each provided firestation's jurisdiction
     * People are grouped into households by address, and resident details are included in response
     *
     * @param stationNumbers
     * @return Json object as string in ResponseEntity
     */
    @GetMapping("/flood/stations")
    public ResponseEntity<String> getHouseholdsByFirestation(@RequestParam("stations") int[] stationNumbers) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/flood/stations", intArrayToStringArray(stationNumbers));
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getHouseholdsByFirestationEntity(stationNumbers, model, finder,
                parser, recordParser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);
        //respond
        return response;
    }

    /**
     * Returns information for a provided firstname/lastname combination
     * Includes name, address, age, medications and allergies
     * <p>
     * While firstname/lastname should be considered unique in the system,
     * if there are multiple this will return all matches
     *
     * @param firstName
     * @param lastName
     * @return Json object as string in ResponseEntity
     */
    @GetMapping("/personInfo")
    public ResponseEntity<String> getPersonInfoByFirstNameLastName(@RequestParam("firstName") String firstName,
                                                                   @RequestParam("lastName") String lastName) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/personInfo", new String[] {firstName, lastName});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getPersonInfoByFirstNameLastNameEntity(firstName, lastName, model, finder,
                                                                                            recordParser, logHandler);

        //Log response
        logHandler.logResponse("GET", response);
        //respond
        return response;
    }

    /**
     * Returns email addresses for all residents of provided city
     *
     * @param city
     * @return Json object as string in ResponseEntity
     */
    @GetMapping("/communityEmail")
    public ResponseEntity<String> getEmailAddressesByCity(@RequestParam("city") String city) {
        //Log reqquest
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/communityEmail", new String[] {city});
        //load data
        SafetyAlertsModel model = loadModelFromDisk();
        if (model == null){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getEmailAddressesByCity(city, model, finder, logHandler);

        //Log response
        logHandler.logResponse("GET", response);
        //respond
        return response;
    }

}