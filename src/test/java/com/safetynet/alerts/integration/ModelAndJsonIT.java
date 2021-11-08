package com.safetynet.alerts.integration;

import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.presentation.model.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ModelAndJsonIT {

    private static JsonHandler jsonHandler;
    private SafetyAlertsModel safetyAlertsModel;
    private static JsonDAO jsonDAO;
    private static File testFile;
    private static File comparisonFile;
    private static String jsonString = "{\n" +
            "    \"persons\": [\n" +
            "        {\"firstName\":\"FirstOne\",\"lastName\":\"LastOne\",\"address\":\"Address\",\"city\":\"City\",\"zip\":\"Zip\",\"phone\":\"555-1234\",\"email\":\"name@mail.com\"},\n" +
            "        {\"firstName\":\"FirstTwo\",\"lastName\":\"LastTwo\",\"address\":\"Address\",\"city\":\"City\",\"zip\":\"Zip\",\"phone\":\"555-1234\",\"email\":\"name@mail.com\"}\n" +
            "    ],\n" +
            "    \"firestations\": [\n" +
            "        {\"address\":\"Address\",\"station\":1},\n" +
            "        {\"address\":\"Address\",\"station\":2}\n" +
            "    ],\n" +
            "    \"medicalrecords\": [\n" +
            "        {\"firstName\":\"FirstOne\",\"lastName\":\"LastOne\",\"birthdate\":\"01/02/1234\",\"medications\":[\"medication\"],\"allergies\":[\"allergy\"]},\n" +
            "        {\"firstName\":\"FirstTwo\",\"lastName\":\"LastTwo\",\"birthdate\":\"01/02/1234\",\"medications\":[\"medication\"],\"allergies\":[\"allergy\"]}\n" +
            "    ]\n" +
            "}";

    @BeforeAll
    private static void setUp() throws IOException {
        jsonDAO = new JsonDAO();
        jsonHandler = new JsonHandler();
        comparisonFile = new File("comparisonFile.json");
        testFile = new File("testFile.json");

        if (!comparisonFile.exists()) {
            comparisonFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(comparisonFile);
        fileWriter.write(jsonString);
        fileWriter.close();
    }

    @AfterAll
    private static void cleanUp() {
        if (comparisonFile.exists()) {
            comparisonFile.delete();
        }
        if (testFile.exists()) {
            testFile.delete();
       }
    }

    @Test
    public void testCreatingModelFromJsonFile() throws IOException {
        //Preparation
        testFile = new File("testFile.json");
        if (!testFile.exists()) {
            testFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(jsonString);
        fileWriter.close();

        //Method
        safetyAlertsModel = jsonHandler.jsonToModel(jsonDAO.readJsonFromFile("testFile.json"));

        //Verification
        assertEquals(2, safetyAlertsModel.getPersons().length);
    }

    @Test
    public void testCreatingJsonFileFromModel() throws IOException {
        //Preparation
        Person[] persons = new Person[]{
                new Person("FirstOne", "LastOne", "Address", "City", "Zip", "555-1234", "name@mail.com"),
                new Person("FirstTwo", "LastTwo", "Address", "City", "Zip", "555-1234", "name@mail.com")
        };
        Firestation[] firestations = new Firestation[]{
                new Firestation("Address", 1),
                new Firestation("Address", 2)
        };
        MedicalRecord[] medicalRecords = new MedicalRecord[]{
                new MedicalRecord("FirstOne", "LastOne", "01/02/1234", new String[]{"medication"}, new String[]{"allergy"}),
                new MedicalRecord("FirstTwo", "LastTwo", "01/02/1234", new String[]{"medication"}, new String[]{"allergy"})
        };
        safetyAlertsModel = new SafetyAlertsModel(persons, firestations, medicalRecords);

        testFile = new File("testFile.json");

        //Method
        jsonDAO.writeJsonToFile("testFile.json", jsonHandler.modelToJson(safetyAlertsModel));

        //Verification
        assertTrue(FileUtils.contentEquals(testFile, comparisonFile));
    }


}
