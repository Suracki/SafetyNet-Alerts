package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.controller.OutputBuilder;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GetService {

    public ResponseEntity<String> getPeopleServicedByStation(int stationNumber, SafetyAlertsModel model, ModelObjectFinder finder,
                                                             CollectionParser parser, OutputBuilder builder,
                                                             PersonAndRecordParser recordParser) {

        //Perform Request
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, model);
        if (firestations.length == 0) {
            //No mappings found for this Firestation number. Return 'not found'.
            return ResponseEntity.notFound().build();
        }
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);

        builder.reset();

        for (Person person : peopleAtAddress) {
            builder.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
            if (recordParser.isAChild(person, model.getMedicalRecords())) {
                builder.addChild();
            } else {
                builder.addAdult();
            }
        }
        //Generate response
        String responseString = builder.getPeopleServicedByStationResult();
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }

    public ResponseEntity<String> getChildrenAtAddress(String address, SafetyAlertsModel model, ModelObjectFinder finder,
                                                       OutputBuilder builder, PersonAndRecordParser recordParser) {
        //Perform Request
        Person[] peopleAtAddress = finder.findPersonByAddress(new String[] {address},model);
        builder.reset();
        for (Person person : peopleAtAddress) {
            if (recordParser.isAChild(person, model.getMedicalRecords())) {
                builder.addChildPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
            }
            else {
                builder.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
            }
        }
        String responseString = builder.getChildrenAtAddressResult(recordParser);


        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }


    public ResponseEntity<String> getPhoneNumbersForPeopleServicedByStation(int stationNumber, SafetyAlertsModel model, ModelObjectFinder finder,
                                                                            OutputBuilder builder, CollectionParser parser){
        Firestation[] firestations = finder.findFirestationByNumber(stationNumber, model);
        if (firestations.length == 0) {
            //No mappings found for this Firestation number. Return 'not found'.
            return ResponseEntity.notFound().build();
        }
        String[] addresses = parser.getAddressesFromFirestationMappings(firestations);
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
        builder.reset();
        for (Person person : peopleAtAddress) {
            builder.addPhone(person.getPhone());
        }
        String responseString = builder.getPhoneNumbersForStationResult();

        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }


    public ResponseEntity<String> getFirestationNumberAndResidentsForAddress(String address, SafetyAlertsModel model, ModelObjectFinder finder,
                                                                             OutputBuilder builder, CollectionParser parser,
                                                                             PersonAndRecordParser recordParser){
        Firestation firestation = finder.findFirestation(address, model);
        if (firestation == null) {
            //No mappings found for this address Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
        Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);

        builder.reset();
        builder.addFirestation(firestation);

        for (Person person : peopleAtAddress) {
            builder.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
        }
        String responseString = builder.getFirestationNumberAndResidentsForAddressResult(recordParser);

        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }

    public ResponseEntity<String> getHouseholdsByFirestation(int[] stationNumbers, SafetyAlertsModel model, ModelObjectFinder finder,
                                                                             OutputBuilder builder, CollectionParser parser,
                                                                             PersonAndRecordParser recordParser){
        builder.reset();

        ArrayList<Firestation> firestationsMappings = new ArrayList<Firestation>();
        for (int stationNumber : stationNumbers) {
            Firestation[] foundStationMappings = finder.findFirestationByNumber(stationNumber, model);
            for (Firestation firestation : foundStationMappings) {
                firestationsMappings.add(firestation);
            }
        }
        if (firestationsMappings.size() == 0) {
            //No mappings found for the provided station numbers. Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        //Loop through firestation mappings and get people, create households and add them to builder
        builder.setStationNumbers(stationNumbers);
        for (Firestation firestation : firestationsMappings) {
            String[] addresses = parser.getAddressesFromFirestationMappings(new Firestation[] {firestation});
            for (String address : addresses) {
                Household household = new Household(address, firestation.getStation());
                Person[] peopleAtAddress = finder.findPersonByAddress(addresses, model);
                for (Person person : peopleAtAddress) {
                    household.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
                }
                builder.addHousehold(household);
            }

        }

        String responseString = builder.getHouseholdsByFirestationResult(recordParser);

        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }

    public ResponseEntity<String> getPersonInfoByFirstNameLastName(String firstName, String lastName, SafetyAlertsModel model,
                                                                   ModelObjectFinder finder, OutputBuilder builder){
        Person[] persons = finder.findPersons(firstName, lastName, model);
        builder.reset();

        if (persons.length == 0) {
            //No people found with matching first name/last name. Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        for (Person person : persons) {
            builder.addPerson(person, finder.findMedicalRecord(person.getFirstName(), person.getLastName(), model));
        }

        String responseString = builder.getPersonInfoByFirstNameLastNameResult(new PersonAndRecordParser());

        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }

    public ResponseEntity<String> getEmailAddressesByCity(String city, SafetyAlertsModel model, ModelObjectFinder finder,
                                                          OutputBuilder builder){
        Person[] persons = finder.findPersonByCity(city, model);
        builder.reset();

        if (persons.length == 0) {
            //No people found for this city. Return 'not found'.
            return ResponseEntity.notFound().build();
        }

        for (Person person : persons) {
            builder.addPerson(person, new MedicalRecord());
        }

        String responseString = builder.getEmailAddressesByCityResult();

        //respond
        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> response = new ResponseEntity<String>(responseString, responseHeaders, HttpStatus.OK);
        return response;
    }
}
