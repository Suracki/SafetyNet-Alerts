package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * ModelObjectFinder is used to find specific objects or groups of objects in our model
 */
@Service
public class ModelObjectFinder {

    /**
     * Find a person in the model by firstname/lastname
     *
     * @param firstName
     * @param lastName
     * @param model
     * @return Person object with matching name, or null if none found
     */
    public Person findPerson(String firstName, String lastName, SafetyAlertsModel model) {

        for (Person person : model.getPersons()){
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)){
                return person;
            }
        }
        return null;
    }

    /**
     * Find all people with the same firstname/lastname combination
     *
     * @param firstName
     * @param lastName
     * @param model
     * @return array of Person objects with matching names
     */
    public Person[] findPersons(String firstName, String lastName, SafetyAlertsModel model) {
        ArrayList<Person> persons = new ArrayList<Person>();
        for (Person person : model.getPersons()){
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)){
                persons.add(person);
            }
        }

        Person[] personsWithName = new Person[persons.size()];
        int index = 0;
        for (Person person : persons) {
            personsWithName[index] = person;
            index++;
        }
        return personsWithName;
    }

    /**
     * Find all people at provided address
     * Can check for multiple addresses at once
     *
     * @param addresses array of address strings
     * @param model
     * @return array of Person objects
     */
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

    /**
     * Find all people living in a specific city
     *
     * @param city
     * @param model
     * @return array of Person objects
     */
    public Person[] findPersonByCity(String city, SafetyAlertsModel model){
        ArrayList<Person> persons = new ArrayList<Person>();
        for (Person person : model.getPersons()){
            if (person.getCity().equals(city)) {
                persons.add(person);
            }
        }
        Person[] personsAtCity = new Person[persons.size()];
        int index = 0;
        for (Person person : persons) {
            personsAtCity[index] = person;
            index++;
        }
        return personsAtCity;
    }

    /**
     * Find the MedicalRecord for a person with provided firstname/lastname
     *
     * @param firstName
     * @param lastName
     * @param model
     * @return MedicalRecord object, or null if none found
     */
    public MedicalRecord findMedicalRecord(String firstName, String lastName, SafetyAlertsModel model) {

        for (MedicalRecord record : model.getMedicalRecords()){
            if (record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)){
                return record;
            }
        }
        return null;
    }

    /**
     * Find a firestation mapping for a specific address
     *
     * @param address
     * @param model
     * @return Firestation object, or null if none found
     */
    public Firestation findFirestation(String address, SafetyAlertsModel model) {
        for (Firestation firestation : model.getFirestations()){
            if (firestation.getAddress().equals(address)){
                return firestation;
            }
        }
        return null;
    }

    /**
     * Find all firestation mappings for a specific firestation number
     *
     * @param stationNumber
     * @param model
     * @return array of Firestation objects
     */
    public Firestation[] findFirestationByNumber(int stationNumber, SafetyAlertsModel model) {
        ArrayList<Firestation> stationMappings = new ArrayList<Firestation>();
        for (Firestation firestation : model.getFirestations()){
            if (firestation.getStation() == stationNumber) {
                stationMappings.add(firestation);
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
