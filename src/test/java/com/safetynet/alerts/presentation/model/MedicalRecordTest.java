package com.safetynet.alerts.presentation.model;

import com.safetynet.alerts.presentation.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MedicalRecordTest {

    private static MedicalRecord medicalRecord;

    @BeforeEach
    private void setUp() {
        medicalRecord = new MedicalRecord("First", "Last", "01/02/1234",
                new String[] {"one", "two"}, new String[] {"three", "four"});
    }

    @Test
    public void medicalRecordShouldHaveGettersForAllVars() {

        //Preparation
        //Covered in BeforeEach

        //Method & Verification
        assertEquals("First", medicalRecord.getFirstName());
        assertEquals("Last", medicalRecord.getLastName());
        assertEquals("01/02/1234", medicalRecord.getBirthdate());
        assertArrayEquals(new String[] {"one", "two"}, medicalRecord.getMedications());
        assertArrayEquals(new String[] {"three", "four"}, medicalRecord.getAllergies());
    }

    @Test
    public void medicalRecordShouldHaveSettersForAllVars() {

        //Preparation
        //Covered in BeforeEach

        //Method
        medicalRecord.setFirstName("Newfirst");
        medicalRecord.setLastName("Newlast");
        medicalRecord.setBirthdate("02/03/5678");
        medicalRecord.setMedications(new String[]{"five", "six"});
        medicalRecord.setAllergies(new String[]{"seven", "eight"});

        //Verification
        assertEquals("Newfirst", medicalRecord.getFirstName());
        assertEquals("Newlast", medicalRecord.getLastName());
        assertEquals("02/03/5678", medicalRecord.getBirthdate());
        assertArrayEquals(new String[] {"five", "six"}, medicalRecord.getMedications());
        assertArrayEquals(new String[] {"seven", "eight"}, medicalRecord.getAllergies());
    }

    @Test
    public void medicalRecordShouldBeAbleToAddMedications() {
        //Preparation
        String[] newMedications = addElement(medicalRecord.getMedications(), "new");

        //Method
        medicalRecord.addMedication("new");

        //Verification
        assertArrayEquals(newMedications, medicalRecord.getMedications());
    }

    @Test
    public void medicalRecordShouldBeAbleToAddAllergies(){
        //Preparation
        String[] newSymptoms = addElement(medicalRecord.getAllergies(), "new");

        //Method
        medicalRecord.addAllergy("new");

        //Verification
        assertArrayEquals(newSymptoms, medicalRecord.getAllergies());
    }

    private String[] addElement(String[] array, String element) {
        String[] updatedArray = new String[array.length + 1];
        System.arraycopy(array, 0, updatedArray, 0,array.length);
        updatedArray[array.length] = element;
        return updatedArray;
    }

}
