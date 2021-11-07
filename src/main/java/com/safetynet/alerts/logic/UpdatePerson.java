package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.stereotype.Service;

@Service
public class UpdatePerson {

    public ResultModel addPerson(ModelObjectFinder finder, SafetyAlertsModel model, Person person) {
        if (finder.findPerson(person.getFirstName(), person.getLastName(), model) == null) {
            model.addPerson(person);
            return new ResultModel(model, true);
        }
        else {
            return new ResultModel(model, false);
        }
    }

    public ResultModel updatePerson(ModelObjectFinder finder, SafetyAlertsModel model, Person newPerson) {
        Person oldPerson = finder.findPerson(newPerson.getFirstName(), newPerson.getLastName(), model);
        if (oldPerson != null) {
            Person[] persons = model.getPersons();
            for (Person currentPerson : persons) {
                if (currentPerson.getFirstName().equals(newPerson.getFirstName())
                        && currentPerson.getLastName().equals(newPerson.getLastName())) {
                    currentPerson.update(newPerson);
                }
            }
            model.setPersons(persons);

            return new ResultModel(model, true);
        }
        else {
            return new ResultModel(model, false);
        }
    }

}
