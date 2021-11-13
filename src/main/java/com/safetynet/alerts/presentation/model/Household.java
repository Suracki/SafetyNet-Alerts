package com.safetynet.alerts.presentation.model;

import java.util.ArrayList;
import java.util.TreeMap;

public class Household {

    TreeMap<Person, MedicalRecord> people;
    private String address;
    private int station;

    public Household(String address, int station) {
        this.address = address;
        this.station = station;
        people = new TreeMap<Person, MedicalRecord>(new PersonComparator());
    }

    public void addPerson(Person person, MedicalRecord record) {
        people.put(person, record);
    }

    public TreeMap<Person, MedicalRecord> getPeople() {
        return people;
    }

    public String getAddress(){
        return address;
    }

    public int getStation() {
        return station;
    }
}
