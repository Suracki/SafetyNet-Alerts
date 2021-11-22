package com.safetynet.alerts.presentation.model.entity;

import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;

import java.util.ArrayList;

public class PersonInfoByFnLnResponseEntity {

    private ArrayList<PersonAllInfo> persons;

    public PersonInfoByFnLnResponseEntity(){
        persons = new ArrayList<>();
    }

    public void addPerson(Person person, MedicalRecord record, int age) {
        persons.add(new PersonAllInfo(person, record, age));
    }


    private class PersonAllInfo {

        private String firstName;
        private String lastName;
        private String address;
        private String city;
        private String zip;
        private int age;
        private String email;
        private String[] medications;
        private String[] allergies;

        public PersonAllInfo(Person person, MedicalRecord record, int age) {
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
            this.address = person.getAddress();
            this.city = person.getCity();
            this.zip = person.getZip();
            this.age = age;
            this.email = person.getEmail();
            this.medications = record.getMedications();
            this.allergies = record.getAllergies();
        }

    }
}
