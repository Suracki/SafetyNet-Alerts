package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
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

    public MedicalRecord findRecord(String firstName, String lastName, SafetyAlertsModel model) {

        for (MedicalRecord record : model.getMedicalRecords()){
            if (record.getFirstName().equals(firstName) && record.getLastName().equals(lastName)){
                return record;
            }
        }
        return null;
    }

    public Firestation findFirestation(String address, SafetyAlertsModel model){
        for (Firestation firestation : model.getFirestations()){
            if (firestation.getAddress().equals(address)){
                return firestation;
            }
        }
        return null;
    }

}
