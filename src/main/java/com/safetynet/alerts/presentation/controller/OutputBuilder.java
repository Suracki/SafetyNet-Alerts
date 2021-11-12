package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.logic.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.PersonComparator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OutputBuilder {

    TreeMap<Person, MedicalRecord> persons;
    TreeMap<Person, MedicalRecord> childPersons;
    int children;
    int adults;


    public OutputBuilder() {
        persons = new TreeMap<Person, MedicalRecord>(new PersonComparator());
        childPersons = new TreeMap<Person, MedicalRecord>(new PersonComparator());
        children = 0;
        adults = 0;
    }

    public void addPerson(Person person, MedicalRecord record) {
        persons.put(person, record);
    }

    public void addChildPerson(Person person, MedicalRecord record) {
        childPersons.put(person, record);
    }

    public void addChild() {
        children++;
    }

    public void addAdult() {
        adults++;
    }

    public String getPeopleServicedByStationResult() {
        if (persons.size() == 0) {
            //No people added yet
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\"persons\": [\n");
        for (Map.Entry<Person, MedicalRecord> entry : persons.entrySet()) {
            builder.append("    (\"firstName\":\"")
                    .append(entry.getKey().getFirstName())
                    .append("\",\"lastName\":\"")
                    .append(entry.getKey().getLastName())
                    .append("\",\"address\":\"")
                    .append(entry.getKey().getAddress())
                    .append("\",\"city\":\"")
                    .append(entry.getKey().getCity())
                    .append("\",\"zip\":\"")
                    .append(entry.getKey().getZip())
                    .append("\",\"phone\":\"")
                    .append(entry.getKey().getPhone())
                    .append("\"),\n");
        }
        //remove final ,\n
        builder.setLength(builder.length() - 2);

        builder.append("\n],\n\"adults\":[" + adults + "],\"children\":[" + children + "]}");
        return builder.toString();
    }

    public String getChildrenAtAddressResult(PersonAndRecordParser parser) {
        if (childPersons.size() == 0) {
            //No children added yet
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\"children\": [\n");
        for (Map.Entry<Person, MedicalRecord> entry : childPersons.entrySet()) {
            int age = parser.getAge(entry.getValue());
            builder.append("    (\"firstName\":\"")
                    .append(entry.getKey().getFirstName())
                    .append("\",\"lastName\":\"")
                    .append(entry.getKey().getLastName())
                    .append("\",\"age\":\"")
                    .append(age)
                    .append("\"),\n");
        }
        //remove final ,\n
        builder.setLength(builder.length() - 2);

        builder.append("\n]\n{\"adults\": [\n");
        for (Map.Entry<Person, MedicalRecord> entry : persons.entrySet()) {
            builder.append("    (\"firstName\":\"")
                    .append(entry.getKey().getFirstName())
                    .append("\",\"lastName\":\"")
                    .append(entry.getKey().getLastName())
                    .append("\"),\n");
        }
        //remove final ,\n
        builder.setLength(builder.length() - 2);

        builder.append("\n]}");
        return builder.toString();
    }

}
