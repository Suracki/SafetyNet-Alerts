package com.safetynet.alerts.presentation.model.entity;

import com.safetynet.alerts.presentation.model.Person;

import java.util.ArrayList;

public class PeopleServicedByStationResponseEntity {

    private ArrayList<PersonNoEmail> persons;
    private int adults;
    private int children;

    public PeopleServicedByStationResponseEntity() {
        persons = new ArrayList<>();
        adults = 0;
        children = 0;
    }

    public void addPerson(Person person) {
        persons.add(new PersonNoEmail(person));
    }

    public void addChild() {
        children++;
    }

    public void addAdult() {
        adults++;
    }

    private class PersonNoEmail {

        private String firstName;
        private String lastName;
        private String address;
        private String city;
        private String zip;
        private String phone;

        public PersonNoEmail(Person person) {
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
            this.address = person.getAddress();
            this.city = person.getCity();
            this.zip = person.getZip();
            this.phone = person.getPhone();
        }

    }
}
