package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.stereotype.Service;

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

}
