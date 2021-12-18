package com.safetynet.alerts.logic.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.logic.parsers.CollectionParser;
import com.safetynet.alerts.logic.parsers.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.entity.*;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * GetService generates ResponseEntities for each of the custom Get request endpoints
 */
@Service
public class GetService extends BaseService {

    @Autowired
    CollectionParser parser;
    @Autowired
    PersonAndRecordParser recordParser;

    /**
     * Returns all people serviced by a specific fire station.
     * Includes first name, last name, address, and phone number for each person
     * Also includes summary of the number of adults and children in the service area
     *
     * @param stationNumber the number of the firestation
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getPeopleServicedByStationEntity(int stationNumber) {
        //Find firestation mappings for provided station number
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, safetyAlertsModel);
        //Get all addresses served by this station
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        //Find all people living at these addresses
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, safetyAlertsModel);

        PeopleServicedByStationResponseEntity entity = new PeopleServicedByStationResponseEntity();

        //Loop through all people at the address and add them to output entity
        for (Person person : peopleAtAddress) {
            entity.addPerson(person);
            if (recordParser.isAChild(person, safetyAlertsModel.getMedicalRecords())) {
                logHandler.debug("GetService" ,"Adding Child to Entity");
                entity.addChild();
            } else {
                logHandler.debug("GetService" , "Adding Adult to Entity");
                entity.addAdult();
            }
        }
        //Generate response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    /**
     * Returns all children (people under 18) at a specified address.
     * Includes first name, last name, age for each child
     * Also includes separated list of adults living at the address
     * If no children are at the address, response contains an empty string
     *
     * @param address the residence address
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getChildrenAtAddressEntity(String address) {
        //Find al people living at this address
        Person[] peopleAtAddress = finder.findPersonByAddress(new String[] {address},safetyAlertsModel);

        ChildrenAtAddressResponseEntity entity = new ChildrenAtAddressResponseEntity();

        //Loop through all people at the address and add them to output entity
        for (Person person : peopleAtAddress) {
            if (recordParser.isAChild(person, safetyAlertsModel.getMedicalRecords())) {
                logHandler.debug("GetService" ,"Adding Child to Entity");
                entity.addChild(person, recordParser.getAge(
                        finder.findMedicalRecord(person.getFirstName(), person.getLastName(), safetyAlertsModel)));
            }
            else {
                logHandler.debug("GetService" ,"Adding Adult to Entity");
                entity.addAdult(person);
            }
        }

        //Generate response
        if (!entity.childrenAtAddress()) {
            //If there are no children at address, return empty string
            logHandler.debug("GetService" ,"No children found at address");
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<>("", responseHeaders, HttpStatus.OK);
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    /**
     * Returns a list of phone numbers for each person within the provided firestation's jurisdiction
     *
     * @param stationNumber the number of the firestation
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getPhoneNumbersForPeopleServicedByStationEntity(int stationNumber){
        //Find firestation mappings for provided station number
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, safetyAlertsModel);
        //Get all addresses served by this station
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        //Find all people living at these addresses
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, safetyAlertsModel);

        PhoneNumbersForPeopleServicedByStationEntity entity = new PhoneNumbersForPeopleServicedByStationEntity();

        //Loop through all people at the address, add their phone number to output entity
        for (Person person : peopleAtAddress) {
            logHandler.debug("GetService" ,"Adding Phonenumber to Entity");
            entity.addPhone(person.getPhone());
        }

        //Generate response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }


    /**
     * Returns the firestation number that services a provided address
     * Also includes details of all people living at the address
     *
     * @param address the residence address
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getFirestationNumberAndResidentsForAddressEntity(String address){
        //Find firestation mapping for provided address
        Firestation firestation = finder.findFirestation(address, safetyAlertsModel);

        FirestationNumberAndResidentsForAddressEntity entity = new FirestationNumberAndResidentsForAddressEntity();

        if (firestation == null) {
            //No mappings found for this address, return empty json.
            logHandler.debug("GetService" ,"Address not found in Firestation mappings");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.setPrettyPrinting().create();
            String responseString = gson.toJson(entity);
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
        }

        //Get address from firestation mapping
        String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});

        //Find all people living at address
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, safetyAlertsModel);

        entity.addFirestation(firestation);

        //Loop through all people at the address and add them to output entity
        for (Person person : peopleAtAddress) {
            logHandler.debug("GetService" ,"Adding Person to Entity");
            entity.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), safetyAlertsModel),
                    recordParser.getAge(finder.findMedicalRecord(person.getFirstName(), person.getLastName(), safetyAlertsModel)));
        }

        //Generate response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    /**
     * Returns a list of all households in each provided firestation's jurisdiction
     * People are grouped into households by address, and resident details are included in response
     *
     * @param stationNumbers collection of firestation numbers
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getHouseholdsByFirestationEntity(int[] stationNumbers){
        ArrayList<Firestation> firestationsMappings = new ArrayList<>();
        //Loop through provided firestation numbers, and for each get their firestation mappings
        for (int stationNumber : stationNumbers) {
            Firestation[] foundStationMappings = finder.findFirestationByNumber(stationNumber, safetyAlertsModel);
            firestationsMappings.addAll(Arrays.asList(foundStationMappings));
        }

        HouseholdsByFirestationResponseEntity entity = new HouseholdsByFirestationResponseEntity();

        //Loop through firestation mappings
        for (Firestation firestation : firestationsMappings) {
            //Find households for each firestation
            logHandler.debug("GetService" ,"Address mapping found for firestation " + firestation.getStation());
            String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
            ArrayList<HouseholdEntity> households = new ArrayList<>();
            for (String address : addresses) {
                //Create household object for each address
                logHandler.debug("GetService" ,"Creating household");
                HouseholdEntity household = new HouseholdEntity(address, firestation.getStation());
                Person[] peopleAtAddress = finder.findPersonByAddress(addresses, safetyAlertsModel);
                for (Person person : peopleAtAddress) {
                    //Add each person at address to the household object
                    logHandler.debug("GetService" ,"Adding person to Household");
                    int age = recordParser.getAge(finder.findMedicalRecord(person.getFirstName(), person.getLastName(), safetyAlertsModel));
                    household.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), safetyAlertsModel), age);
                }
                logHandler.debug("GetService" ,"Adding Household to Entity");
                households.add(household);
            }
            //Add firestation & its households to output entity
            entity.addStation(firestation, households);
        }

        //Generate response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    /**
     * Returns information for a provided firstname/lastname combination
     * Includes name, address, age, medications and allergies
     * <p>
     * While firstname/lastname should be considered unique in the system,
     * if there are multiple this will return all matches
     * @param firstName
     * @param lastName
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getPersonInfoByFirstNameLastNameEntity(String firstName, String lastName){
        //Get all people with matching firstname/lastname
        Person[] persons = finder.findPersons(firstName, lastName, safetyAlertsModel);

        PersonInfoByFnLnResponseEntity entity = new PersonInfoByFnLnResponseEntity();

        //For each person, add the required information to the output entity
        for (Person person : persons) {
            logHandler.debug("GetService" ,"Adding Person to Entity");
            int age = recordParser.getAge(finder.findMedicalRecord(person.getFirstName(), person.getLastName(), safetyAlertsModel));
            entity.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), safetyAlertsModel), age);
        }

        //Generate response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    /**
     * Returns email addresses for all residents of provided city
     *
     * @param city
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> getEmailAddressesByCity(String city){
        //Get all people living at specified city
        Person[] persons = finder.findPersonByCity(city, safetyAlertsModel);

        EmailAddressesByCityResultEntity entity = new EmailAddressesByCityResultEntity();

        //For each person, add their email address to the entity
        for (Person person : persons) {
            if (person.getCity().equals(city)) {
                logHandler.debug("GetService" ,"Adding Email to Entity");
                entity.addEmail(person.getEmail());
            }
        }

        //Generate response
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }
}
