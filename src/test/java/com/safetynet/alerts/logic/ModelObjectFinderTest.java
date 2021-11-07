package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ModelObjectFinderTest {
    private static SafetyAlertsModel model;

    @BeforeAll
    public static void setUp() {
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
        model = new SafetyAlertsModel(persons, firestations, medicalRecords);
    }

    @Test
    public void modelObjectFinderCanFindAPersonByName() {
        //Preparation
        ModelObjectFinder finder = new ModelObjectFinder();
        String firstName = "FirstOne";
        String lastName = "LastOne";

        //Method
        Person person = finder.findPerson(firstName, lastName, model);

        //Verification
        assertEquals("FirstOne", person.getFirstName());
        assertEquals("LastOne", person.getLastName());
    }

    @Test
    public void modelObjectFinderReturnsNullIfPersonNotFound() {
        //Preparation
        ModelObjectFinder finder = new ModelObjectFinder();
        String firstName = "FirstThree";
        String lastName = "LastThree";

        //Method
        Person person = finder.findPerson(firstName, lastName, model);

        //Verification
        assertNull(person);
    }


}
