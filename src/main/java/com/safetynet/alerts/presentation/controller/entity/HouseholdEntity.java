package com.safetynet.alerts.presentation.controller.entity;

import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;

import java.util.ArrayList;

public class HouseholdEntity {

    ArrayList<PersonFnLnPnAgeMedAlrgy> people;
    private String address;
    private int station;

    public HouseholdEntity(String address, int station) {
        this.address = address;
        this.station = station;
        people = new ArrayList<>();
    }

    public void addPerson(Person person, MedicalRecord record, int age) {
        people.add(new PersonFnLnPnAgeMedAlrgy(person, record, age));
    }

    public String getAddress(){
        return address;
    }

    public int getStation() {
        return station;
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
