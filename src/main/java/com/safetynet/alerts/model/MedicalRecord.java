package com.safetynet.alerts.model;

public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthdate;
    private String[] medications;
    private String[] allergies;

    public MedicalRecord(String firstName, String lastName, String birthdate, String[] medications, String[] allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String[] getMedications() {
        return medications;
    }

    public void setMedications(String[] medications) {
        this.medications = medications;
    }

    public String[] getAllergies() {
        return allergies;
    }

    public void setAllergies(String[] allergies) {
        this.allergies = allergies;
    }

    public void addAllergy(String newAllergy) {
        allergies = addElement(allergies, newAllergy);
    }

    public void addMedication(String newMedication) {
        medications = addElement(medications, newMedication);
    }

    private String[] addElement(String[] array, String element) {
        String[] updatedArray = new String[array.length + 1];
        for (int i = 0; i < array.length; i++){
            updatedArray[i] = array[i];
        }
        updatedArray[array.length] = element;
        return updatedArray;
    }


}
