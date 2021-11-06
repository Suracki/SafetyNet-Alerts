package com.safetynet.alerts.presentation.data.io;

import com.safetynet.alerts.data.io.JsonDAO;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class JsonDAOTest {

    private static JsonDAO jsonDAO;
    private static File testFile;
    private static File comparisonFile;
    private String jsonString = "{\n" +
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



    @BeforeAll
    private static void setUp() {
        jsonDAO = new JsonDAO();
    }

    @BeforeEach
    private void createComparisonFile() throws IOException {
        comparisonFile = new File("comparisonFile.json");
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
    public void jsonDAOCanWriteJsonStringToNewFile() throws IOException {
        //Preparation
        testFile = new File("testFile.json");
        if (testFile.exists()) {
            testFile.delete();
        }

        //Method
        jsonDAO.writeJsonToFile("testFile.json", jsonString);

        //Verification
        assertTrue(FileUtils.contentEquals(testFile, comparisonFile));
    }

    @Test
    public void jsonDAOCanWriteJsonStringToExistingFile() throws IOException {
        //Preparation
        testFile = new File("testFile.json");

        //Method
        jsonDAO.writeJsonToFile("testFile.json", jsonString);

        //Verification
        testFile = new File("testFile.json");
        assertTrue(FileUtils.contentEquals(testFile, comparisonFile));
    }

    @Test
    public void jsonDAOCanReadJsonStringFromFile() throws IOException {
        //Preparation
        //Covered in BeforeAll & BeforeEach

        //Method
//        String fileFromDAO = jsonDAO.readJsonFromFile("comparisonFile.json")
//                .replace("\n", "").replace("\r", "");
        String fileFromDAO = jsonDAO.readJsonFromFile("comparisonFile.json");
        //Verification
        //assertEquals(jsonString.replace("\n", "").replace("\r", ""), fileFromDAO);
        assertEquals(jsonString, fileFromDAO);
    }

}
