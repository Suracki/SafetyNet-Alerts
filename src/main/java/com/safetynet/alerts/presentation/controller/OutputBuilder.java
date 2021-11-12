package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;

import java.util.HashMap;
import java.util.Map;

public class OutputBuilder {

    HashMap<Person, MedicalRecord> persons;
    int children;
    int adults;


    public OutputBuilder() {
        persons = new HashMap<Person, MedicalRecord>();
        children = 0;
        adults = 0;
    }

    public void addPerson(Person person, MedicalRecord record) {
        persons.put(person, record);
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
            return "{}";
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

}
