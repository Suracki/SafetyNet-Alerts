package com.safetynet.alerts.presentation.model;

import com.safetynet.alerts.presentation.model.Person;

import java.util.Comparator;

public class PersonComparator implements Comparator<Person> {

    public int compare(Person person1, Person person2) {
        return person1.getFirstName().compareToIgnoreCase(person2.getFirstName());
    }

}
