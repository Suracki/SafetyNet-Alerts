package com.safetynet.alerts.logic;

import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
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
                new Person("FirstOne", "LastOne", "AddressOne", "CityOne", "Zip", "555-1234", "name@mail.com"),
                new Person("FirstTwo", "LastTwo", "AddressOne", "CityTwo", "Zip", "555-1234", "name@mail.com"),
                new Person("FirstTwo", "LastTwo", "AddressThree", "CityThree", "Zip", "555-1234", "name@mail.com"),
                new Person("FirstThree", "LastThree", "AddressTwo", "CityOne", "Zip", "555-1234", "name@mail.com")
        };
        Firestation[] firestations = new Firestation[]{
                new Firestation("AddressOne", 1),
                new Firestation("AddressTwo", 2),
                new Firestation("AddressThree", 2)
        };
        MedicalRecord[] medicalRecords = new MedicalRecord[]{
                new MedicalRecord("FirstOne", "LastOne", "01/02/2010", new String[]{"medication"}, new String[]{"allergy"}),
                new MedicalRecord("FirstTwo", "LastTwo", "01/02/1995", new String[]{"medication"}, new String[]{"allergy"}),
                new MedicalRecord("FirstThree", "LastThree", "01/02/2000", new String[]{"medication"}, new String[]{"allergy"})
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
    public void modelObjectFinderCanFindMultiplePeopleWithSameName() {
        //Preparation
        ModelObjectFinder finder = new ModelObjectFinder();
        String firstName = "FirstTwo";
        String lastName = "LastTwo";

        //Method
        Person[] persons = finder.findPersons(firstName, lastName, model);

        //Verification
        assertEquals("FirstTwo", persons[0].getFirstName());
        assertEquals("FirstTwo", persons[1].getFirstName());
    }

    @Test
    public void modelObjectFinderReturnsNullIfPersonNotFound() {
        //Preparation
        ModelObjectFinder finder = new ModelObjectFinder();
        String firstName = "Doesnt";
        String lastName = "Exist";

        //Method
        Person person = finder.findPerson(firstName, lastName, model);

        //Verification
        assertNull(person);
    }

    @Test
    public void modelObjectFinderCanFindAFirestationMappingByAddress() {
        //Preparation
        ModelObjectFinder finder = new ModelObjectFinder();
        String address = "AddressOne";

        //Method
        int station = finder.findFirestation(address, model).getStation();

        //Verification
        assertEquals(1, station);
    }

    @Test
    public void modelObjectFinderCanFindFirestationMappingsByStationNumber() {
        //Preparation
        ModelObjectFinder finder = new ModelObjectFinder();
        int stationNumber = 2;

        //Method
        Firestation[] foundStations = finder.findFirestationByNumber(stationNumber, model);

        //Verification
        assertEquals(2, foundStations.length);
        assertEquals("AddressTwo", foundStations[0].getAddress());
    }

    @Test
    public void modelObjectFinderCanFindMedicalRecordsByName() {
        //Preparation
        ModelObjectFinder finder = new ModelObjectFinder();
        String firstName = "FirstOne";
        String lastName = "LastOne";

        //Method
        MedicalRecord foundRecord = finder.findMedicalRecord(firstName, lastName, model);

        //Verification
        assertEquals("01/02/2010", foundRecord.getBirthdate());
    }

    @Test
    public void modelObjectFinderCanFindAllPeopleLivingAtAnAddress() {
        //Preparation
        ModelObjectFinder finder = new ModelObjectFinder();
        String[] address = {"AddressOne"};

        //Method
        Person[] foundPersons = finder.findPersonByAddress(address, model);

        //Verification
        assertEquals(2, foundPersons.length);
        assertEquals("FirstOne", foundPersons[0].getFirstName());
        assertEquals("FirstTwo", foundPersons[1].getFirstName());

    }

    @Test
    public void modelObjectFinderCanFindAllPeopleLivingInACity() {
        //Preparation
        ModelObjectFinder finder = new ModelObjectFinder();
        String city = "CityOne";

        //Method
        Person[] foundPersons = finder.findPersonByCity(city, model);

        //Verification
        assertEquals(2, foundPersons.length);
        assertEquals("FirstOne", foundPersons[0].getFirstName());
        assertEquals("FirstThree", foundPersons[1].getFirstName());
    }


}
