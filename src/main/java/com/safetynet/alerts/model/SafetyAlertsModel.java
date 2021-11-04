package com.safetynet.alerts.model;

public class SafetyAlertsModel {

    private Person[] persons;
    private Firestation[] firestations;
    private MedicalRecord[] medicalRecords;

    public SafetyAlertsModel() {
        persons = new Person[0];
        firestations = new Firestation[0];
        medicalRecords = new MedicalRecord[0];
    }

    public SafetyAlertsModel(Person[] persons, Firestation[] firestations, MedicalRecord[] medicalRecords) {
        this.persons = persons;
        this.firestations = firestations;
        this.medicalRecords = medicalRecords;
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
        MedicalRecord[] updatedArray = new MedicalRecord[medicalRecords.length + 1];
        System.arraycopy(medicalRecords, 0, updatedArray, 0,medicalRecords.length);
        updatedArray[medicalRecords.length] = medicalRecord;
        medicalRecords = updatedArray;
    }

    public Person[] getPersons() {
        return persons.clone();
    }

    public Firestation[] getFirestations() {
        return firestations.clone();
    }

    public MedicalRecord[] getMedicalRecords() {
        return medicalRecords.clone();
    }
}
