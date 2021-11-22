package com.safetynet.alerts.logic;

import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.updaters.ResultModel;
import com.safetynet.alerts.logic.updaters.UpdatePerson;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdatePersonTest {
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
    public void UpdatePersonCanAddNewPerson() {
        //Preparation
        UpdatePerson updatePerson = new UpdatePerson();
        Person newPerson = new Person("FirstThree", "LastThree", "Address", "City", "Zip",
                "555-1234", "name@mail.com");

        //Method

        ResultModel result = updatePerson.addPerson(new ModelObjectFinder(), model, newPerson);

        //Verification
        Person[] updatedPersons = result.getModel().getPersons();
        assertTrue(result.getBool());
        assertEquals(3, updatedPersons.length);
    }

    @Test
    public void UpdatePersonCanAddUpdateExistingPerson() {
        //Preparation
        UpdatePerson updatePerson = new UpdatePerson();
        Person newPerson = new Person("FirstOne", "LastOne", "NewAddress", "City", "NewZip",
                "555-1234", "name@mail.com");

        //Method

        ResultModel result = updatePerson.updatePerson(new ModelObjectFinder(), model, newPerson);

        //Verification
        Person[] updatedPersons = result.getModel().getPersons();

        assertTrue(result.getBool());
        assertEquals(2, updatedPersons.length);
        assertEquals("NewAddress", updatedPersons[0].getAddress());
        assertEquals("City", updatedPersons[0].getCity());
    }

    @Test
    public void UpdatePersonCanDeleteExistingPerson() {
        //Preparation
        UpdatePerson updatePerson = new UpdatePerson();
        Person removePerson = new Person("FirstTwo", "LastTwo", "Address", "City", "Zip",
                "555-1234", "name@mail.com");

        //Method

        ResultModel result = updatePerson.deletePerson(new ModelObjectFinder(), model, removePerson);

        //Verification
        Person[] updatedPersons = result.getModel().getPersons();
        assertTrue(result.getBool());
        assertEquals(1, updatedPersons.length);
    }



}
