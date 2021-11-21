package com.safetynet.alerts.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.presentation.controller.entity.*;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class GetService {

    public ResponseEntity<String> getPeopleServicedByStationEntity(int stationNumber, SafetyAlertsModel model, ModelObjectFinder finder,
                                                                   CollectionParser parser,
                                                                   PersonAndRecordParser recordParser,
                                                                   LogHandlerTiny logHandlerTiny) {
        //Perform Request
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, model);
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
        PeopleServicedByStationResponseEntity entity = new PeopleServicedByStationResponseEntity();

        for (Person person : peopleAtAddress) {
            entity.addPerson(person);
            if (recordParser.isAChild(person, model.getMedicalRecords())) {
                logHandlerTiny.debug("GetService" ,"Adding Child to Entity");
                entity.addChild();
            } else {
                logHandlerTiny.debug("GetService" , "Adding Adult to Entity");
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

    public ResponseEntity<String> getChildrenAtAddressEntity(String address, SafetyAlertsModel model, ModelObjectFinder finder,
                                                             PersonAndRecordParser recordParser,
                                                             LogHandlerTiny logHandlerTiny) {
        //Perform Request
        Person[] peopleAtAddress = finder.findPersonByAddress(new String[] {address},model);
        ChildrenAtAddressResponseEntity entity = new ChildrenAtAddressResponseEntity();

        for (Person person : peopleAtAddress) {
            if (recordParser.isAChild(person, model.getMedicalRecords())) {
                logHandlerTiny.debug("GetService" ,"Adding Child to Entity");
                entity.addChild(person, recordParser.getAge(
                        finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model)));
            }
            else {
                logHandlerTiny.debug("GetService" ,"Adding Adult to Entity");
                entity.addAdult(person);
            }
        }
        //respond
        if (!entity.childrenAtAddress()) {
            logHandlerTiny.debug("GetService" ,"No children found at address");
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<>("", responseHeaders, HttpStatus.OK);
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> getPhoneNumbersForPeopleServicedByStationEntity(int stationNumber, SafetyAlertsModel model,
                                                                                  ModelObjectFinder finder, CollectionParser parser,
                                                                                  LogHandlerTiny logHandlerTiny){
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, model);
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
        PhoneNumbersForPeopleServicedByStationEntity entity = new PhoneNumbersForPeopleServicedByStationEntity();
        for (Person person : peopleAtAddress) {
            logHandlerTiny.debug("GetService" ,"Adding Phonenumber to Entity");
            entity.addPhone(person.getPhone());
        }

        //respond
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }


    public ResponseEntity<String> getFirestationNumberAndResidentsForAddressEntity(String address, SafetyAlertsModel model, ModelObjectFinder finder,
                                                                                   CollectionParser parser, PersonAndRecordParser recordParser,
                                                                                   LogHandlerTiny logHandlerTiny){
        Firestation firestation = finder.findFirestation(address, model);
        FirestationNumberAndResidentsForAddressEntity entity = new FirestationNumberAndResidentsForAddressEntity();

        if (firestation == null) {
            //No mappings found for this address, return empty json.
            logHandlerTiny.debug("GetService" ,"Address not found in Firestation mappings");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.setPrettyPrinting().create();
            String responseString = gson.toJson(entity);
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
        }

        String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);

        entity.addFirestation(firestation);
        for (Person person : peopleAtAddress) {
            logHandlerTiny.debug("GetService" ,"Adding Person to Entity");
            entity.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model),
                    recordParser.getAge(finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model)));
        }

        //respond
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> getHouseholdsByFirestationEntity(int[] stationNumbers, SafetyAlertsModel model, ModelObjectFinder finder,
                                                                   CollectionParser parser, PersonAndRecordParser recordParser,
                                                                   LogHandlerTiny logHandlerTiny){
        ArrayList<Firestation> firestationsMappings = new ArrayList<>();
        for (int stationNumber : stationNumbers) {
            Firestation[] foundStationMappings = finder.findFirestationByNumber(stationNumber, model);
            firestationsMappings.addAll(Arrays.asList(foundStationMappings));
        }

        HouseholdsByFirestationResponseEntity entity = new HouseholdsByFirestationResponseEntity();

        for (Firestation firestation : firestationsMappings) {
            //Find households for each firestation
            logHandlerTiny.debug("GetService" ,"Address mapping found for firestation " + firestation.getStation());
            String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
            ArrayList<HouseholdEntity> households = new ArrayList<>();
            for (String address : addresses) {
                logHandlerTiny.debug("GetService" ,"Creating household");
                HouseholdEntity household = new HouseholdEntity(address, firestation.getStation());
                Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
                for (Person person : peopleAtAddress) {
                    logHandlerTiny.debug("GetService" ,"Adding person to Household");
                    int age = recordParser.getAge(finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
                    household.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model), age);
                }
                logHandlerTiny.debug("GetService" ,"Adding Household to Entity");
                households.add(household);
            }
            entity.addStation(firestation, households);
        }

        //respond
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> getPersonInfoByFirstNameLastNameEntity(String firstName, String lastName, SafetyAlertsModel model,
                                                                        ModelObjectFinder finder,
                                                                        PersonAndRecordParser recordParser,
                                                                        LogHandlerTiny logHandlerTiny){
        Person[] persons = finder.findPersons(firstName, lastName, model);

        PersonInfoByFnLnResponseEntity entity = new PersonInfoByFnLnResponseEntity();

        for (Person person : persons) {
            logHandlerTiny.debug("GetService" ,"Adding Person to Entity");
            int age = recordParser.getAge(finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
            entity.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model), age);
        }

        //respond
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }

    public ResponseEntity<String> getEmailAddressesByCity(String city, SafetyAlertsModel model, ModelObjectFinder finder,
                                                          LogHandlerTiny logHandlerTiny){
        Person[] persons = finder.findPersonByCity(city, model);

        EmailAddressesByCityResultEntity entity = new EmailAddressesByCityResultEntity();

        for (Person person : persons) {
            if (person.getCity().equals(city)) {
                logHandlerTiny.debug("GetService" ,"Adding Email to Entity");
                entity.addEmail(person.getEmail());
            }
        }

        //respond
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(entity);
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(responseString, responseHeaders, HttpStatus.OK);
    }
}
