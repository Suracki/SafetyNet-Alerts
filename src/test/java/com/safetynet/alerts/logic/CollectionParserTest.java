package com.safetynet.alerts.logic;

import com.safetynet.alerts.logic.parsers.CollectionParser;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionParserTest {
    private static CollectionParser parser;
    private static SafetyAlertsModel model;

    @BeforeAll
    public static void setUp() {
        parser = new CollectionParser();
        Person[] persons = new Person[]{
                new Person("FirstOne", "LastOne", "Address", "City", "Zip",
                        "555-1234", "name@mail.com"),
                new Person("FirstTwo", "LastTwo", "Address", "City", "Zip",
                        "555-1234", "name@mail.com")
        };
        Firestation[] firestations = new Firestation[]{
                new Firestation("Address", 1),
                new Firestation("Address", 2),
                new Firestation("AddressTwo", 3),
                new Firestation("AddressThree", 1),
                new Firestation("AddressFour", 2)
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
    public void CollectionParserCanGetListOfUniqueAddressesFromFirestationMappings() {
        //Prepare
        String[] addresses;

        //Method
        addresses = parser.getAddressesFromFirestationMappings(model.getFirestations());

        //Verification
        assertEquals(4, addresses.length);
    }

}
