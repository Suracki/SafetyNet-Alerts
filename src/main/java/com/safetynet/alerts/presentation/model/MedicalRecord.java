package com.safetynet.alerts.presentation.model;

public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthdate;
    private String[] medications;
    private String[] allergies;

    public MedicalRecord(){

    }

    public MedicalRecord(String firstName, String lastName, String birthdate, String[] medications, String[] allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications.clone();
        this.allergies = allergies.clone();
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
        return medications.clone();
    }

    public void setMedications(String[] medications) {
        this.medications = medications.clone();
    }

    public String[] getAllergies() {
        return allergies.clone();
    }

    public void setAllergies(String[] allergies) {
        this.allergies = allergies.clone();
    }

    public void addAllergy(String newAllergy) {
        allergies = addElement(allergies, newAllergy);
    }

    public void addMedication(String newMedication) {
        medications = addElement(medications, newMedication);
    }

    private String[] addElement(String[] array, String element) {
        String[] updatedArray = new String[array.length + 1];
        System.arraycopy(array, 0, updatedArray, 0,array.length);
        updatedArray[array.length] = element;
        return updatedArray;
    }

    public void update(MedicalRecord medicalRecord) {
        this.firstName = medicalRecord.getFirstName();
        this.lastName = medicalRecord.getLastName();
        this.birthdate = medicalRecord.getBirthdate();
        this.medications = medicalRecord.getMedications();
        this.allergies = medicalRecord.getAllergies();
    }

    @Override
    public String toString() {
        String output = "{\"firstName\":\"" + firstName +
                "\", \"lastName\":\"" + lastName +
                "\", \"birthdate\":\"" + birthdate +
                "\", \"medications\":\"" + stringArrayToString(medications) +
                "\", \"allergies\":\"" + stringArrayToString(allergies) + "\"}";

        return output;
    }

    private String stringArrayToString(String[] stringArray) {
        StringBuilder builder = new StringBuilder();
        if (stringArray.length == 0) {
            return "[]";
        }
        builder.append("[");
        for (String string : stringArray) {
            builder.append("\"")
                    .append(string)
                    .append("\",");
        }
        //remove final ,
        builder.setLength(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }


}
