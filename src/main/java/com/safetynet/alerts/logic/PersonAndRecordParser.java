package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * PersonAndRecordParser is used to cross reference a Person object with a MedicalRecord object to find specific details
 */
@Service
public class PersonAndRecordParser {

    /**
     * Check if specified person is a child.
     * Looks for medical record matching provided Person and then calculates age
     * If no medical record is found, returns false/adult
     *
     * @param person
     * @param records array of MedicalRecord objects
     * @return true if under 18, false if over 18
     */
    public boolean isAChild(Person person, MedicalRecord[] records) {
        for (MedicalRecord record : records) {
            if (person.getFirstName().equals(record.getFirstName()) &&
                    person.getLastName().equals(record.getLastName())) {
                //Is this person under 18
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate dob = LocalDate.parse(record.getBirthdate(), formatter);
                LocalDate currentDate = LocalDate.now();
                if (Period.between(dob, currentDate).getYears() < 18){
                    //Person is a child, return true
                    return true;
                }
            }
        }
        //Person is not a child, return false
        return false;
    }

    /**
     * Returns the age in years of a person from a provided MedicalRecord
     *
     * @param record
     * @return age in integer form
     */
    public int getAge(MedicalRecord record) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dob = LocalDate.parse(record.getBirthdate(), formatter);
        LocalDate currentDate = LocalDate.now();


        return Period.between(dob, currentDate).getYears();
    }

}
