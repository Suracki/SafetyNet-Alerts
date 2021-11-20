package com.safetynet.alerts.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.presentation.controller.entity.*;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GetService {

    public ResponseEntity<String> getPeopleServicedByStationEntity(int stationNumber, SafetyAlertsModel model, ModelObjectFinder finder,
                                                             CollectionParser parser,
                                                             PersonAndRecordParser recordParser) {
        //Perform Request
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, model);
        if (firestations.length == 0) {
            //No mappings found for this Firestation number. Return 'not found'.
            return ResponseEntity.notFound().build();
        }
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
        PeopleServicedByStationResponseEntity entity = new PeopleServicedByStationResponseEntity();

        for (Person person : peopleAtAddress) {
            entity.addPerson(person);
            if (recordParser.isAChild(person, model.getMedicalRecords())) {
                entity.addChild();
            } else {
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
                                                       PersonAndRecordParser recordParser) {
        //Perform Request
        Person[] peopleAtAddress = finder.findPersonByAddress(new String[] {address},model);
        ChildrenAtAddressResponseEntity entity = new ChildrenAtAddressResponseEntity();

        for (Person person : peopleAtAddress) {
            if (recordParser.isAChild(person, model.getMedicalRecords())) {

                entity.addChild(person, recordParser.getAge(
                        finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model)));
            }
            else {
                entity.addAdult(person);
            }
        }
        //respond
        if (!entity.childrenAtAddress()) {
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
                                                                            ModelObjectFinder finder, CollectionParser parser){
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, model);
        if (firestations.length == 0) {
            //No mappings found for this Firestation number. Return 'not found'.
            return ResponseEntity.notFound().build();
        }
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
        PhoneNumbersForPeopleServicedByStationEntity entity = new PhoneNumbersForPeopleServicedByStationEntity();
        for (Person person : peopleAtAddress) {
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
                                                                             CollectionParser parser, PersonAndRecordParser recordParser){
        Firestation firestation = finder.findFirestation(address, model);
        if (firestation == null) {
            //No mappings found for this address Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
        FirestationNumberAndResidentsForAddressEntity entity = new FirestationNumberAndResidentsForAddressEntity();

        entity.addFirestation(firestation);
        for (Person person : peopleAtAddress) {
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
                                                                   CollectionParser parser, PersonAndRecordParser recordParser){
        ArrayList<Firestation> firestationsMappings = new ArrayList<>();
        for (int stationNumber : stationNumbers) {
            Firestation[] foundStationMappings = finder.findFirestationByNumber(stationNumber, model);
            firestationsMappings.addAll(Arrays.asList(foundStationMappings));
        }
        if (firestationsMappings.size() == 0) {
            //No mappings found for the provided station numbers. Return 'not found'.
            return ResponseEntity.notFound().build();
        }
        //firestationsMappings = collection of firestations

        HouseholdsByFirestationResponseEntity entity = new HouseholdsByFirestationResponseEntity();

        for (Firestation firestation : firestationsMappings) {
            //Find households for each firestation
            String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
            ArrayList<HouseholdEntity> households = new ArrayList<>();
            for (String address : addresses) {
                HouseholdEntity household = new HouseholdEntity(address, firestation.getStation());
                Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
                for (Person person : peopleAtAddress) {
                    int age = recordParser.getAge(finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
                    household.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model), age);
                }
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
                                                                   PersonAndRecordParser recordParser){
        Person[] persons = finder.findPersons(firstName, lastName, model);

        if (persons.length == 0) {
            //No people found with matching first name/last name. Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        PersonInfoByFnLnResponseEntity entity = new PersonInfoByFnLnResponseEntity();

        for (Person person : persons) {
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

    public ResponseEntity<String> getEmailAddressesByCity(String city, SafetyAlertsModel model, ModelObjectFinder finder){
        Person[] persons = finder.findPersonByCity(city, model);

        if (persons.length == 0) {
            //No people found for this city. Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        EmailAddressesByCityResultEntity entity = new EmailAddressesByCityResultEntity();

        for (Person person : persons) {
            if (person.getCity().equals(city)) {
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
