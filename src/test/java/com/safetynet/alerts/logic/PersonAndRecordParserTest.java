package com.safetynet.alerts.logic;

import com.safetynet.alerts.logic.parsers.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PersonAndRecordParserTest {

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
                new MedicalRecord("FirstOne", "LastOne", "01/02/1995",
                        new String[]{"medicationOne","medicationTwo"}, new String[]{"allergy"}),
                new MedicalRecord("FirstTwo", "LastTwo", "01/02/2015",
                        new String[]{"medicationThree"}, new String[]{"allergyOne","allergyTwo"})
        };
        model = new SafetyAlertsModel(persons, firestations, medicalRecords);
    }

    @Test
    public void personAndRecordParserReturnsTrueIfPersonIsChild() {
        //Prepare
        PersonAndRecordParser parser = new PersonAndRecordParser();
        Person testChild = model.getPersons()[1];

        //Method
        boolean resultChild = parser.isAChild(testChild, model.getMedicalRecords());

        //Verification
        assertTrue(resultChild);
    }

    @Test
    public void personAndRecordParserReturnsFalseIfPersonIsAdult() {
        //Prepare
        PersonAndRecordParser parser = new PersonAndRecordParser();
        Person testAdult = model.getPersons()[0];

        //Method
        boolean resultAdult = parser.isAChild(testAdult, model.getMedicalRecords());

        //Verification
        assertFalse(resultAdult);
    }

    @Test
    public void personAndRecordParserReturnsPersonsAge() {
        //Prepare
        PersonAndRecordParser parser = new PersonAndRecordParser();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        MedicalRecord testAdult = model.getMedicalRecords()[0];
        LocalDate adultDate = LocalDate.now().minusYears(26);
        testAdult.setBirthdate(adultDate.format(formatter));

        //Method
        int adultAge = parser.getAge(testAdult);

        //Verification
        assertEquals(26, adultAge);
    }


}
