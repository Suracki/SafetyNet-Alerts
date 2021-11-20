package com.safetynet.alerts.presentation.controller.entity;

import com.safetynet.alerts.logic.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;

import java.util.ArrayList;

public class ChildrenAtAddressResponseEntity {

    private ArrayList<ChildFnLnAge> children;
    private ArrayList<AdultFnLn> adults;

    public ChildrenAtAddressResponseEntity() {
        children = new ArrayList<>();
        adults = new ArrayList<>();
    }

    public void addChild(Person person, int age) {
        children.add(new ChildFnLnAge(person, age));
    }

    public void addAdult(Person person) {
        adults.add(new AdultFnLn(person));
    }

    public boolean childrenAtAddress() {
        return children.size() > 0;
    }


    private class ChildFnLnAge {

        private String firstName;
        private String lastName;
        private int age;

        public ChildFnLnAge(Person person, int age) {
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
            this.age = age;
        }

    }

    private class AdultFnLn {

        private String firstName;
        private String lastName;

        public AdultFnLn(Person person) {
            this.firstName = person.getFirstName();
            this.lastName = person.getLastName();
        }

    }


}
