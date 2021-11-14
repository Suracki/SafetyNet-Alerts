package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.logic.PersonAndRecordParser;
import com.safetynet.alerts.presentation.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@Service
public class OutputBuilder {

    private TreeMap<Person, MedicalRecord> persons;
    private TreeMap<Person, MedicalRecord> childPersons;
    private ArrayList<String> phoneNumbers;
    private ArrayList<Household> households;
    private int[] stationNumbers;
    private Firestation firestation;
    private int children;
    private int adults;


    public OutputBuilder() {
        persons = new TreeMap<Person, MedicalRecord>(new PersonComparator());
        childPersons = new TreeMap<Person, MedicalRecord>(new PersonComparator());
        phoneNumbers = new ArrayList<String>();
        households = new ArrayList<Household>();
        firestation = null;
        children = 0;
        adults = 0;
        stationNumbers = new int[0];
    }

    public void reset() {
        persons = new TreeMap<Person, MedicalRecord>(new PersonComparator());
        childPersons = new TreeMap<Person, MedicalRecord>(new PersonComparator());
        phoneNumbers = new ArrayList<String>();
        households = new ArrayList<Household>();
        firestation = null;
        children = 0;
        adults = 0;
        stationNumbers = new int[0];
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

    public void addPhone(String phoneNumber) {
        phoneNumbers.add(phoneNumber);
    }

    public void addFirestation(Firestation firestation) {
        this.firestation = firestation;
    }

    public void addHousehold(Household household) {
        households.add(household);
    }
    public void setStationNumbers(int[] stationNumbers) {
        this.stationNumbers = stationNumbers;
    }

    public String getPeopleServicedByStationResult() {
        if (persons.size() == 0) {
            //No people added yet
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\n\"persons\": [\n");
        for (Map.Entry<Person, MedicalRecord> entry : persons.entrySet()) {
            builder.append("        {\"firstName\":\"")
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
                    .append("\"},\n");
        }
        //remove final ,\n
        builder.setLength(builder.length() - 2);

        builder.append("\n    ],\n    \"adults\":[" + adults + "],\"children\":[" + children + "]\n}");
        return builder.toString();
    }

    public String getChildrenAtAddressResult(PersonAndRecordParser parser) {
        if (childPersons.size() == 0) {
            //No children added yet
            return "{[]}";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\n    \"children\": [\n");
        for (Map.Entry<Person, MedicalRecord> entry : childPersons.entrySet()) {
            int age = parser.getAge(entry.getValue());
            builder.append("        {\"firstName\":\"")
                    .append(entry.getKey().getFirstName())
                    .append("\",\"lastName\":\"")
                    .append(entry.getKey().getLastName())
                    .append("\",\"age\":\"")
                    .append(age)
                    .append("\"},\n");
        }
        //remove final ,\n
        builder.setLength(builder.length() - 2);

        builder.append("\n    ],\n    \"adults\": [\n");
        for (Map.Entry<Person, MedicalRecord> entry : persons.entrySet()) {
            builder.append("        {\"firstName\":\"")
                    .append(entry.getKey().getFirstName())
                    .append("\",\"lastName\":\"")
                    .append(entry.getKey().getLastName())
                    .append("\"},\n");
        }
        //remove final ,\n
        builder.setLength(builder.length() - 2);

        builder.append("\n    ]\n}");
        return builder.toString();
    }

    public String getPhoneNumbersForStationResult() {
        if (phoneNumbers.size() == 0) {
            //No numbers added yet
            return "";
        }
        StringBuilder builder = new StringBuilder();

        builder.append("{\n    \"phoneNumbers\": [\n");
        for (String phone : phoneNumbers) {
            builder.append("        {\"phone\":\"")
                    .append(phone)
                    .append("\"},\n");
        }
        //remove final ,\n
        builder.setLength(builder.length() - 2);

        builder.append("\n    ]\n}");
        return builder.toString();

    }

    public String getFirestationNumberAndResidentsForAddressResult(PersonAndRecordParser parser) {

        if (persons.size() == 0) {
            //No people added yet
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\n    \"firestations\": [\n")
                .append("        {\"address\":\"")
                .append(firestation.getAddress())
                .append("\",\"station\":")
                .append(firestation.getStation())
                .append("}\n");
        builder.append("    ],\n    \"persons\": [\n");
        for (Map.Entry<Person, MedicalRecord> entry : persons.entrySet()) {
            int age = parser.getAge(entry.getValue());
            builder.append("        {\"firstName\":\"")
                    .append(entry.getKey().getFirstName())
                    .append("\",\"lastName\":\"")
                    .append(entry.getKey().getLastName())
                    .append("\",\"phone\":\"")
                    .append(entry.getKey().getPhone())
                    .append("\",\"Age\":\"")
                    .append(age)
                    .append("\",\"medications\":")
                    .append(stringArrayToString(entry.getValue().getMedications()))
                    .append(",\"allergies\":")
                    .append(stringArrayToString(entry.getValue().getAllergies()))
                    .append("},\n");
        }
        //remove final ,\n
        builder.setLength(builder.length() - 2);

        builder.append("\n    ]\n}");
        return builder.toString();

    }

    private String stringArrayToString(String[] stringArray) {
        StringBuilder builder = new StringBuilder();
        if (stringArray.length == 0) {
            return "[]";
        }
        builder.append("[");
        for (String string : stringArray) {
            builder.append("\"")
                    .append(string)
                    .append("\",");
        }
        //remove final ,
        builder.setLength(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

    public String getHouseholdsByFirestationResult(PersonAndRecordParser parser) {
        if (households.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();

        builder.append("{\n    \"firestations\": [\n");
        for (int station : stationNumbers) {
            builder.append("        {\"station\":" + station + ",\"households\":[\n");
            boolean found = false;
            for (Household household : households) {
                if (household.getStation() == station) {
                    found = true;
                    builder.append("            {\"address\": \"" + household.getAddress() + "\",\"persons\":[\n");
                    TreeMap<Person, MedicalRecord> people = household.getPeople();
                    for (Map.Entry<Person, MedicalRecord> entry : people.entrySet()) {
                        int age = parser.getAge(entry.getValue());
                        builder.append("                {\"firstName\":\"")
                                .append(entry.getKey().getFirstName())
                                .append("\",\"lastName\":\"")
                                .append(entry.getKey().getLastName())
                                .append("\",\"phone\":\"")
                                .append(entry.getKey().getPhone())
                                .append("\",\"Age\":\"")
                                .append(age)
                                .append("\",\"medications\":")
                                .append(stringArrayToString(entry.getValue().getMedications()))
                                .append(",\"allergies\":")
                                .append(stringArrayToString(entry.getValue().getAllergies()))
                                .append("},\n");
                    }
                    //remove final ,
                    builder.setLength(builder.length() - 2);
                    builder.append("\n            ]},\n");

                }
            }
            //remove final ,
            if (found) {
                builder.setLength(builder.length() - 2);
                builder.append("\n        ]},\n");
            }
            else {
                builder.setLength(builder.length() - 1);
                builder.append("]},\n");
            }



        }
        //remove final ,
        builder.setLength(builder.length() - 2);
        builder.append("\n    ]\n}");
        return builder.toString();


    }

    public String getPersonInfoByFirstNameLastNameResult(PersonAndRecordParser parser) {
        if (persons.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();

        builder.append("{\n    \"persons\": [\n");

        for (Map.Entry<Person, MedicalRecord> entry : persons.entrySet()) {
            int age = parser.getAge(entry.getValue());
            builder.append("        {\"firstName\":\"")
                    .append(entry.getKey().getFirstName())
                    .append("\",\"lastName\":\"")
                    .append(entry.getKey().getLastName())
                    .append("\",\"address\":\"")
                    .append(entry.getKey().getAddress())
                    .append("\",\"city\":\"")
                    .append(entry.getKey().getCity())
                    .append("\",\"zip\":\"")
                    .append(entry.getKey().getZip())
                    .append("\",\"Age\":\"")
                    .append(age)
                    .append("\",\"email\":\"")
                    .append(entry.getKey().getEmail())
                    .append("\",\"medications\":")
                    .append(stringArrayToString(entry.getValue().getMedications()))
                    .append(",\"allergies\":")
                    .append(stringArrayToString(entry.getValue().getAllergies()))
                    .append("},\n");
        }
        //remove final ,
        builder.setLength(builder.length() - 2);

        builder.append("\n    ]\n}");

        return builder.toString();
    }

    public String getEmailAddressesByCityResult() {
        if (persons.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("{\n    \"residentEmails\": [\n");
        for (Map.Entry<Person, MedicalRecord> entry : persons.entrySet()) {
            builder.append("        {\"email\":\"")
                   .append(entry.getKey().getEmail())
                   .append("\"},\n");
        }
        //remove final ,
        builder.setLength(builder.length() - 2);

        builder.append("\n    ]\n}");

        return builder.toString();

    }

}
