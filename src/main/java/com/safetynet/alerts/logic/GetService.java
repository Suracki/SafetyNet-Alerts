package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.controller.OutputBuilder;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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




}
