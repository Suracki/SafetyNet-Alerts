package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateMedicalRecordTest {
    private static SafetyAlertsModel model;

    @BeforeEach
    public void setUp() {
        Person[] persons = new Person[]{
                new Person("FirstOne", "LastOne", "Address", "City", "Zip",
                        "555-1234", "name@mail.com"),
                new Person("FirstTwo", "LastTwo", "Address", "City", "Zip",
                        "555-1234", "name@mail.com")
        };
        Firestation[] firestations = new Firestation[]{
                new Firestation("Address", 1),
                new Firestation("AddressTwo", 2)
        };
        MedicalRecord[] medicalRecords = new MedicalRecord[]{
                new MedicalRecord("FirstOne", "LastOne", "01/02/1234",
                        new String[]{"medication"}, new String[]{"allergy"}),
                new MedicalRecord("FirstTwo", "LastTwo", "01/02/1234",
                        new String[]{"medication"}, new String[]{"allergy"})
        };
        model = new SafetyAlertsModel(persons, firestations, medicalRecords);
    }

    @Test
    public void UpdateMedicalRecordCanAddNewMedicalRecord() {
        //Preparation
        UpdateMedicalRecord updateMedicalRecord = new UpdateMedicalRecord();
        MedicalRecord newMedicalRecord = new MedicalRecord("FirstThree", "LastThree", "01/02/1234",
                new String[]{"medication"}, new String[]{"allergy"});

        //Method

        ResultModel result = updateMedicalRecord.addMedicalRecord(new ModelObjectFinder(), model, newMedicalRecord);

        //Verification
        MedicalRecord[] updatedMedicalRecords = result.getModel().getMedicalRecords();
        assertTrue(result.getBool());
        assertEquals(3, updatedMedicalRecords.length);
    }

    @Test
    public void UpdateMedicalRecordCanUpdateExistingMedicalRecord() {
        //Preparation
        UpdateMedicalRecord updateMedicalRecord = new UpdateMedicalRecord();
        MedicalRecord newMedicalRecord = new MedicalRecord("FirstOne", "LastOne", "01/02/1234",
                new String[]{"medication","medication2"}, new String[]{"allergy","allergy2","allergy3"});

        //Method

        ResultModel result = updateMedicalRecord.updateMedicalRecord(new ModelObjectFinder(), model, newMedicalRecord);

        //Verification
        MedicalRecord[] updatedMedicalRecords = result.getModel().getMedicalRecords();

        assertTrue(result.getBool());
        assertEquals(2, updatedMedicalRecords.length);
        assertEquals(2, updatedMedicalRecords[0].getMedications().length);
        assertEquals(3, updatedMedicalRecords[0].getAllergies().length);
    }

    @Test
    public void UpdateMedicalRecordCanDeleteExistingMedicalRecord() {
        //Preparation
        UpdateMedicalRecord updateMedicalRecord = new UpdateMedicalRecord();
        MedicalRecord deleteMedicalRecord = new MedicalRecord("FirstOne", "LastOne", "01/02/1234",
                new String[]{"medication"}, new String[]{"allergy"});

        //Method

        ResultModel result = updateMedicalRecord.deleteMedicalRecord(new ModelObjectFinder(), model, deleteMedicalRecord);

        //Verification
        MedicalRecord[] updatedMedicalRecords = result.getModel().getMedicalRecords();

        assertTrue(result.getBool());
        assertEquals(1, updatedMedicalRecords.length);
        assertEquals("FirstTwo", updatedMedicalRecords[0].getFirstName());
    }
}
