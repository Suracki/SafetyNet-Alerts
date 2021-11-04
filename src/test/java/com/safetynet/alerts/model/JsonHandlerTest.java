package com.safetynet.alerts.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonHandlerTest {
    private JsonHandler jsonHandler;

    @BeforeEach
    private void setUp() {
        jsonHandler = new JsonHandler();
    }

    @Test
    public void JsonHandlerShouldConvertModelToJson() {
        //Preparation
        Person[] persons = new Person[]{
                new Person("First", "Last", "Address", "City", "Zip", "555-1234", "name@mail.com"),
                new Person("First", "Last", "Address", "City", "Zip", "555-1234", "name@mail.com")
        };
        Firestation[] firestations = new Firestation[]{
                new Firestation("Address", 1),
                new Firestation("Address", 2)
        };
        MedicalRecord[] medicalRecords = new MedicalRecord[]{
                new MedicalRecord("First", "Last", "01/02/1234", new String[]{"medication"}, new String[]{"allergy"}),
                new MedicalRecord("First", "Last", "01/02/1234", new String[]{"medication"}, new String[]{"allergy"})
        };

        String expectedJson = "{\n" +
                "    \"persons\": [\n" +
                "        {\"firstName\":\"First\",\"lastName\":\"Last\",\"address\":\"Address\",\"city\":\"City\",\"zip\":\"Zip\",\"phone\":\"555-1234\",\"email\":\"name@mail.com\"},\n" +
                "        {\"firstName\":\"First\",\"lastName\":\"Last\",\"address\":\"Address\",\"city\":\"City\",\"zip\":\"Zip\",\"phone\":\"555-1234\",\"email\":\"name@mail.com\"},\n" +
                "    ],\n" +
                "    \"firestations\": [\n" +
                "        {\"address\":\"Address\",\"station\":1},\n" +
                "        {\"address\":\"Address\",\"station\":2},\n" +
                "    ],\n" +
                "    \"medicalrecords\": [\n" +
                "        {\"firstName\":\"First\",\"lastName\":\"Last\",\"birthdate\":\"01/02/1234\",\"medications\":[\"medication\"],\"allergies\":[\"allergy\"]},\n" +
                "        {\"firstName\":\"First\",\"lastName\":\"Last\",\"birthdate\":\"01/02/1234\",\"medications\":[\"medication\"],\"allergies\":[\"allergy\"]},\n" +
                "    ],\n" +
                "}";

        String jsonOutput = "";

        //Method
        jsonOutput = jsonHandler.saveData(persons, firestations, medicalRecords);

        //Verification
        assertEquals(expectedJson, jsonOutput);

    }

    @Test
    public void JsonHandlerShouldCreateModelFromJson() {

    }

}
