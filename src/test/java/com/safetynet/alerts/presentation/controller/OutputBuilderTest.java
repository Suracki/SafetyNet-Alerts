package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.logic.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutputBuilderTest {

    private OutputBuilder outputBuilder;
    private SafetyAlertsModel model;

    @BeforeEach
    public void setUp() {
        outputBuilder = new OutputBuilder();

        Person[] persons = new Person[]{
                new Person("FirstOne", "LastOne", "Address", "City", "Zip",
                        "555-1234", "name@mail.com"),
                new Person("FirstTwo", "LastTwo", "Address", "City", "Zip",
                        "555-1234", "name@mail.com"),
                new Person("FirstThree", "LastThree", "Address", "City", "Zip",
                        "555-1234", "name@mail.com"),
                new Person("LastFour", "LastFour", "AddressTwo", "City", "Zip",
                        "555-1234", "name@mail.com")
        };
        Firestation[] firestations = new Firestation[]{
                new Firestation("Address", 1),
                new Firestation("AddressTwo", 2)
        };
        MedicalRecord[] medicalRecords = new MedicalRecord[]{
                new MedicalRecord("FirstOne", "LastOne", "01/02/1995",
                        new String[]{"medicationOne","medicationTwo"}, new String[]{"allergy"}),
                new MedicalRecord("FirstTwo", "LastTwo", "01/02/1998",
                        new String[]{"medicationThree"}, new String[]{"allergyOne","allergyTwo"}),
                new MedicalRecord("FirstThree", "LastThree", "01/02/2015",
                        new String[]{"medicationThree"}, new String[]{"allergyOne","allergyTwo"}),
                new MedicalRecord("FirstFour", "LastFour", "01/02/2015",
                        new String[]{"medicationThree"}, new String[]{"allergyOne","allergyTwo"})
        };
        model = new SafetyAlertsModel(persons, firestations, medicalRecords);
    }

    @Test
    public void outputBuilderGeneratesStringForGetPeopleServicedByStationResult() {
        //Preparation
        outputBuilder.addPerson(model.getPersons()[2], model.getMedicalRecords()[2]);
        outputBuilder.addChild();
        outputBuilder.addPerson(model.getPersons()[1], model.getMedicalRecords()[1]);
        outputBuilder.addAdult();
        outputBuilder.addPerson(model.getPersons()[0], model.getMedicalRecords()[0]);
        outputBuilder.addAdult();

        String expectedString = "{\"persons\": [\n" +
                "    (\"firstName\":\"FirstOne\",\"lastName\":\"LastOne\",\"address\":\"Address\",\"city\":\"City\",\"zip\":\"Zip\",\"phone\":\"555-1234\"),\n" +
                "    (\"firstName\":\"FirstThree\",\"lastName\":\"LastThree\",\"address\":\"Address\",\"city\":\"City\",\"zip\":\"Zip\",\"phone\":\"555-1234\"),\n" +
                "    (\"firstName\":\"FirstTwo\",\"lastName\":\"LastTwo\",\"address\":\"Address\",\"city\":\"City\",\"zip\":\"Zip\",\"phone\":\"555-1234\")\n" +
                "],\n" +
                "\"adults\":[2],\"children\":[1]}";

        //Method
        String responseString = outputBuilder.getPeopleServicedByStationResult();

        //Verification
        assertEquals(expectedString, responseString);
    }

    @Test
    public void outputBuilderGeneratesStringForGetChildrenAtAddressResult() {
        //Preparation
        outputBuilder.addChildPerson(model.getPersons()[2], model.getMedicalRecords()[2]);
        outputBuilder.addPerson(model.getPersons()[1], model.getMedicalRecords()[1]);
        outputBuilder.addPerson(model.getPersons()[0], model.getMedicalRecords()[0]);

        String expectedString = "{\"children\": [\n" +
                "    (\"firstName\":\"FirstThree\",\"lastName\":\"LastThree\",\"age\":\"6\")\n" +
                "]\n" +
                "{\"adults\": [\n" +
                "    (\"firstName\":\"FirstOne\",\"lastName\":\"LastOne\"),\n" +
                "    (\"firstName\":\"FirstTwo\",\"lastName\":\"LastTwo\")\n" +
                "]}";

        //Method
        String responseString = outputBuilder.getChildrenAtAddressResult(new PersonAndRecordParser());

        //Result
        assertEquals(expectedString, responseString);
    }

    @Test
    public void outputBuilderGeneratesStringForGetPhoneNumbersForStationResult() {
        //Preparation
        outputBuilder.addPhone("555-1111");
        outputBuilder.addPhone("555-2222");
        outputBuilder.addPhone("555-3333");
        String expectedString = "{\"phoneNumbers\": [\n" +
                "    (\"phone\":\"555-1111\"),\n" +
                "    (\"phone\":\"555-2222\"),\n" +
                "    (\"phone\":\"555-3333\")\n" +
                "]}";

        //Method
        String responseString = outputBuilder.getPhoneNumbersForStationResult();

        //Result
        assertEquals(expectedString, responseString);
    }

    @Test
    public void outputBuilderGeneratesStringForGetFirestationNumberAndResidentsForAddressResult() {
        //Preparation
        outputBuilder.addFirestation(model.getFirestations()[0]);
        outputBuilder.addPerson(model.getPersons()[0], model.getMedicalRecords()[0]);
        outputBuilder.addPerson(model.getPersons()[1], model.getMedicalRecords()[1]);
        outputBuilder.addPerson(model.getPersons()[2], model.getMedicalRecords()[2]);
        String expectedString = "{\n" +
                "    \"firestations\": [\n" +
                "        {\"address\":\"Address\",\"station\":1}\n" +
                "    ],\n" +
                "{\n" +
                "    \"persons\": [\n" +
                "        (\"firstName\":\"FirstOne\",\"lastName\":\"LastOne\",\"phone\":\"555-1234\",\"Age\":\"26\",\"medications\":\"[\"medicationOne\",medicationTwo\"]\",\"allergies\":\"[\"allergy\"]\"),\n" +
                "        (\"firstName\":\"FirstThree\",\"lastName\":\"LastThree\",\"phone\":\"555-1234\",\"Age\":\"6\",\"medications\":\"[\"medicationThree\"]\",\"allergies\":\"[\"allergyOne\",allergyTwo\"]\"),\n" +
                "        (\"firstName\":\"FirstTwo\",\"lastName\":\"LastTwo\",\"phone\":\"555-1234\",\"Age\":\"23\",\"medications\":\"[\"medicationThree\"]\",\"allergies\":\"[\"allergyOne\",allergyTwo\"]\")\n" +
                "    ]\n" +
                "}";

        //Method
        String responseString = outputBuilder.getFirestationNumberAndResidentsForAddressResult(new PersonAndRecordParser());

        //Result
        assertEquals(expectedString, responseString);
    }
}
