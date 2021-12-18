package com.safetynet.alerts.presentation.model.entity;

import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;

import java.util.ArrayList;

public class FirestationNumberAndResidentsForAddressEntity {

    private Firestation firestation;
    private ArrayList<PersonFnLnPnAgeMedAlrgy> persons;

    public FirestationNumberAndResidentsForAddressEntity() {
        persons = new ArrayList<>();
    }

    public void addFirestation(Firestation firestation){
        this.firestation = firestation;
    }

    public void addPerson(Person person, MedicalRecord record, int age) {
        persons.add(new PersonFnLnPnAgeMedAlrgy(person, record, age));
    }

    private class PersonFnLnPnAgeMedAlrgy {

        private String firstName;
        private String lastName;
        private String phone;
        private int age;
        private String[] medications;
        private String[] allergies;

        public PersonFnLnPnAgeMedAlrgy(Person person, MedicalRecord record, int age) {
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
            this.phone = person.getPhone();
            this.age = age;
            this.medications = record.getMedications();
            this.allergies = record.getAllergies();
        }
    }
}
