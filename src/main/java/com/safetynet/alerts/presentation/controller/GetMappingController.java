package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.logic.service.GetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * RestController for custom endpoint GET mappings
 */
@RestController
public class GetMappingController extends BaseController{

    @Autowired
    private GetService getService;

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

        //confirm data is loaded
        if (!getService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform request
        ResponseEntity<String> response = getService.getPeopleServicedByStationEntity(stationNumber);
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

        //confirm data is loaded
        if (!getService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getChildrenAtAddressEntity(address);

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

        //confirm data is loaded
        if (!getService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getPhoneNumbersForPeopleServicedByStationEntity(stationNumber);

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
        //Log request
        logHandler.setLogger("GetMappingController");
        logHandler.logRequest("GET","/fire", new String[] {address});
        //confirm data is loaded
        if (!getService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getFirestationNumberAndResidentsForAddressEntity(address);

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
        //confirm data is loaded
        if (!getService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getHouseholdsByFirestationEntity(stationNumbers);

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
        //confirm data is loaded
        if (!getService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getPersonInfoByFirstNameLastNameEntity(firstName, lastName);

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
        //confirm data is loaded
        if (!getService.isDataLoaded()){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            logHandler.logResponse("GET", response);
            return response;
        }

        //Perform Request
        ResponseEntity<String> response = getService.getEmailAddressesByCity(city);

        //Log response
        logHandler.logResponse("GET", response);
        //respond
        return response;
    }

}