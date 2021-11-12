package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.Person;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class PersonAndRecordParser {

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
                    System.out.println("Dob: " + dob + ", Now: " + currentDate + " = Child");
                    return true;
                }
            }
        }
        //Person is not a child, return false
        return false;
    }

    public int getAge(MedicalRecord record) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dob = LocalDate.parse(record.getBirthdate(), formatter);
        LocalDate currentDate = LocalDate.now();


        return Period.between(dob, currentDate).getYears();
    }

}
