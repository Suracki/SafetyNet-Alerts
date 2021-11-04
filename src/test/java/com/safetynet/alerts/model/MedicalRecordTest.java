package com.safetynet.alerts.model;

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
    public void medicalRecordShouldHaveGettersForAllFields() {

        //Preparation
        //Covered in BeforeEach

        //Method & Verification
        assertEquals(medicalRecord.getFirstName(),"First");
        assertEquals(medicalRecord.getLastName(),"Last");
        assertEquals(medicalRecord.getBirthdate(),"01/02/1234");
        assertArrayEquals(medicalRecord.getMedications(),new String[] {"one", "two"});
        assertArrayEquals(medicalRecord.getAllergies(),new String[] {"three", "four"});
    }

    @Test
    public void medicalRecordShouldHaveSettersForAllFields() {

        //Preparation
        //Covered in BeforeEach

        //Method
        medicalRecord.setFirstName("Newfirst");
        medicalRecord.setLastName("Newlast");
        medicalRecord.setBirthdate("02/03/5678");
        medicalRecord.setMedications(new String[]{"five", "six"});
        medicalRecord.setAllergies(new String[]{"seven", "eight"});

        //Verification
        assertEquals(medicalRecord.getFirstName(),"Newfirst");
        assertEquals(medicalRecord.getLastName(),"Newlast");
        assertEquals(medicalRecord.getBirthdate(),"02/03/5678");
        assertArrayEquals(medicalRecord.getMedications(),new String[] {"five", "six"});
        assertArrayEquals(medicalRecord.getAllergies(),new String[] {"seven", "eight"});
    }

    @Test
    public void medicalRecordShouldBeAbleToAddMedications() {
        //Preparation
        String[] newMedications = addElement(medicalRecord.getMedications(), "new");

        //Method
        medicalRecord.addMedication("new");

        //Verification
        assertArrayEquals(medicalRecord.getMedications(), newMedications);
    }

    @Test
    public void medicalRecordShouldBeAbleToAddAllergies(){
        //Preparation
        String[] newSymptoms = addElement(medicalRecord.getAllergies(), "new");

        //Method
        medicalRecord.addAllergy("new");

        //Verification
        assertArrayEquals(medicalRecord.getAllergies(), newSymptoms);
    }

    private String[] addElement(String[] array, String element) {
        String[] updatedArray = new String[array.length + 1];
        System.arraycopy(array, 0, updatedArray, 0,array.length);
        updatedArray[array.length] = element;
        return updatedArray;
    }

}
