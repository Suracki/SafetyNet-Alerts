package com.safetynet.alerts.logic;

import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.logic.updaters.ResultModel;
import com.safetynet.alerts.logic.updaters.UpdateFirestation;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateFirestationTest {
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
    public void UpdateFirestationCanAddNewFirestationMapping() {
        //Preparation
        UpdateFirestation updateFirestation = new UpdateFirestation();
        Firestation newFirestation = new Firestation("AddressThree", 2);

        //Method

        ResultModel result = updateFirestation.addFirestation(new ModelObjectFinder(), model, newFirestation);

        //Verification
        Firestation[] updatedFirestations = result.getModel().getFirestations();
        assertTrue(result.successful());
        assertEquals(3, updatedFirestations.length);
    }

    @Test
    public void UpdateFirestationCanAddUpdateExistingFirestationMapping() {
        //Preparation
        UpdateFirestation updateFirestation = new UpdateFirestation();
        Firestation newFirestation = new Firestation("AddressTwo", 3);

        //Method

        ResultModel result = updateFirestation.updateFirestation(new ModelObjectFinder(), model, newFirestation);

        //Verification
        Firestation[] updatedFirestations = result.getModel().getFirestations();

        assertTrue(result.successful());
        assertEquals(2, updatedFirestations.length);
        assertEquals("AddressTwo", updatedFirestations[1].getAddress());
        assertEquals(3, updatedFirestations[1].getStation());
    }

    @Test
    public void UpdateFirestationCanDeleteExistingFirestationMapping() {
        //Preparation
        UpdateFirestation updateFirestation = new UpdateFirestation();
        Firestation deleteFirestation = new Firestation("Address", 1);

        //Method

        ResultModel result = updateFirestation.deleteFirestation(new ModelObjectFinder(), model, deleteFirestation);

        //Verification
        Firestation[] updatedFirestations = result.getModel().getFirestations();
        assertTrue(result.successful());
        assertEquals(1, updatedFirestations.length);
        assertEquals("AddressTwo", updatedFirestations[0].getAddress());
    }

}
