package com.safetynet.alerts.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SafetyAlertsModelTest {

    private static SafetyAlertsModel model;

    @BeforeEach
    private void setUp() {
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
        model = new SafetyAlertsModel(persons, firestations, medicalRecords);
    }

    @Test
    public void SafetyAlertsModelShouldBeAbleToAddPersons() {
        //Preparation
        Person newPerson = new Person("New", "Last", "Address", "City", "Zip", "555-1234", "name@mail.com");

        //Method
        model.addPerson(newPerson);

        //Verification
        Person[] persons = model.getPersons();
        assertEquals("New",persons[2].getFirstName());

    }

    @Test
    public void SafetyAlertsModelShouldBeAbleToAddFirestations() {
        //Preparation
        Firestation newFirestation = new Firestation("Address", 3);

        //Method
        model.addFirestation(newFirestation);

        //Verification
        Firestation[] firestations = model.getFirestations();
        assertEquals(3,firestations[2].getStation());

    }

    @Test
    public void SafetyAlertsModelShouldBeAbleToAddMedicalRecords() {
        //Preparation
        MedicalRecord newMedicalRecord = new MedicalRecord("New", "Last", "01/02/1234", new String[]{"medication"}, new String[]{"allergy"});

        //Method
        model.addMedicalRecord(newMedicalRecord);

        //Verification
        MedicalRecord[] medicalRecords = model.getMedicalRecords();
        assertEquals("New",medicalRecords[2].getFirstName());

    }


}
