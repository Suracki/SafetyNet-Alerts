package com.safetynet.alerts.presentation.model;

import org.springframework.stereotype.Service;

@Service
public class SafetyAlertsModel {

    private Person[] persons;
    private Firestation[] firestations;
    private MedicalRecord[] medicalrecords;

    public SafetyAlertsModel() {
        persons = new Person[0];
        firestations = new Firestation[0];
        medicalrecords = new MedicalRecord[0];
    }

    public SafetyAlertsModel(Person[] persons, Firestation[] firestations, MedicalRecord[] medicalRecords) {
        this.persons = persons;
        this.firestations = firestations;
        this.medicalrecords = medicalRecords;
    }

    public void addPerson(Person person) {
        Person[] updatedArray = new Person[persons.length + 1];
        System.arraycopy(persons, 0, updatedArray, 0,persons.length);
        updatedArray[persons.length] = person;
        persons = updatedArray;
    }

    public void addFirestation(Firestation firestation) {
        Firestation[] updatedArray = new Firestation[firestations.length + 1];
        System.arraycopy(firestations, 0, updatedArray, 0,firestations.length);
        updatedArray[firestations.length] = firestation;
        firestations = updatedArray;
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        MedicalRecord[] updatedArray = new MedicalRecord[medicalrecords.length + 1];
        System.arraycopy(medicalrecords, 0, updatedArray, 0,medicalrecords.length);
        updatedArray[medicalrecords.length] = medicalRecord;
        medicalrecords = updatedArray;
    }

    public Person[] getPersons() {
        return persons.clone();
    }
    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Firestation[] getFirestations() {
        return firestations.clone();
    }
    public void setFirestations(Firestation[] firestations) {
        this.firestations = firestations;
    }

    public MedicalRecord[] getMedicalRecords() {
        return medicalrecords.clone();
    }
    public void setMedicalRecords(MedicalRecord[] medicalRecords) {
        this.medicalrecords = medicalRecords;
    }
}
