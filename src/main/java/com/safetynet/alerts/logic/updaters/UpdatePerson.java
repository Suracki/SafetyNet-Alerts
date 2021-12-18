package com.safetynet.alerts.logic.updaters;

import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.presentation.model.Person;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.stereotype.Service;

/**
 * Service to make changes to Person objects held in model
 */
@Service
public class UpdatePerson {

    /**
     * Add a Person object to the model
     *
     * @param finder ModelObjectFinder used to parse collection
     * @param model SafetyAlertsModel
     * @param person Person object to be added to the model
     * @return ResultModel containing updated SafetyAlertsModel and a boolean confirming if operation succeeded
     */
    public ResultModel addPerson(ModelObjectFinder finder, SafetyAlertsModel model, Person person) {
        if (finder.findPerson(person.getFirstName(), person.getLastName(), model) == null) {
            model.addPerson(person);
            return new ResultModel(model, true);
        }
        else {
            return new ResultModel(model, false);
        }
    }

    /**
     * Update an existing Person object in the model, using firstname/lastname as identifiers
     *
     * @param finder ModelObjectFinder used to parse collection
     * @param model SafetyAlertsModel
     * @param newPerson updated Person object
     * @return ResultModel containing updated SafetyAlertsModel and a boolean confirming if operation succeeded
     */
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

    /**
     * Remove a Person object from the model, using firstname/lastname as identifiers
     *
     * @param finder ModelObjectFinder used to parse collection
     * @param model SafetyAlertsModel
     * @param removePerson Person object to be removed (variables other than firstname/lastname are not checked)
     * @return ResultModel containing updated SafetyAlertsModel and a boolean confirming if operation succeeded
     */
    public ResultModel deletePerson(ModelObjectFinder finder, SafetyAlertsModel model, Person removePerson) {
        //Confirm person exists
        if (finder.findPerson(removePerson.getFirstName(), removePerson.getLastName(), model) == null) {
            //No Person found with matching firstname/lastname
            return new ResultModel(model, false);
        }
        //Update persons in model
        Person[] persons = model.getPersons();
        Person[] updatedPersons = new Person[persons.length - 1];
        int index = 0;
        for (Person person : persons){
            if (person.getFirstName().equals(removePerson.getFirstName()) &&
                    person.getLastName().equals(removePerson.getLastName())) {
                //Do not copy, remove this person
            }
            else {
                updatedPersons[index] = person;
                index++;
            }
        }
        model.setPersons(updatedPersons);
        //Return updated model
        return new ResultModel(model, true);
    }

}
