package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ModelObjectFinder {

    public Person findPerson(String firstName, String lastName, SafetyAlertsModel model) {

        for (Person person : model.getPersons()){
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)){
                return person;
            }
        }
        return null;
    }

    public Person[] findPersonByAddress(String[] addresses, SafetyAlertsModel model){
        ArrayList<Person> persons = new ArrayList<Person>();
        for (String address : addresses) {
            for (Person person : model.getPersons()){
                if (person.getAddress().equals(address)) {
                    persons.add(person);
                }
            }
        }

        Person[] personsAtAddress = new Person[persons.size()];
        int index = 0;
        for (Person person : persons) {
            personsAtAddress[index] = person;
            index++;
        }
        return personsAtAddress;
    }

    public MedicalRecord findMedicalRecord(String firstName, String lastName, SafetyAlertsModel model) {

        for (MedicalRecord record : model.getMedicalRecords()){
            if (record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)){
                return record;
            }
        }
        return null;
    }

    public Firestation findFirestation(String address, SafetyAlertsModel model) {
        for (Firestation firestation : model.getFirestations()){
            if (firestation.getAddress().equals(address)){
                return firestation;
            }
        }
        return null;
    }

    public Firestation[] findFirestationByNumber(int stationNumber, SafetyAlertsModel model) {
        ArrayList<Firestation> stationMappings = new ArrayList<Firestation>();
        for (Firestation firestation : model.getFirestations()){
            if (firestation.getStation() == stationNumber) {
                stationMappings.add(firestation);
                System.out.println("Adding station: " + stationNumber);
            }
        }
        Firestation[] firestations = new Firestation[stationMappings.size()];
        int index = 0;
        for (Firestation firestation : stationMappings) {
            firestations[index] = firestation;
            index++;
        }
        return firestations;
    }

}
