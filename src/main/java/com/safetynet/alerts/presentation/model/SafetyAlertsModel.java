package com.safetynet.alerts.presentation.model;

import com.safetynet.alerts.configuration.DataConfig;
import com.safetynet.alerts.data.io.JsonDAO;
import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.JsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@ConfigurationProperties("static")
public class SafetyAlertsModel {

    @Autowired
    private transient JsonHandler jsonHandler;
    @Autowired
    private transient JsonDAO jsonDAO;
    @Autowired
    private transient DataConfig dataConfig;
    @Autowired
    private transient LogHandlerTiny logHandler;

    private Person[] persons;
    private Firestation[] firestations;
    private MedicalRecord[] medicalrecords;
    private transient boolean dataLoaded;

    public SafetyAlertsModel() {
        persons = new Person[0];
        firestations = new Firestation[0];
        medicalrecords = new MedicalRecord[0];
        dataLoaded = false;
    }

    public SafetyAlertsModel(Person[] persons, Firestation[] firestations, MedicalRecord[] medicalRecords) {
        this.persons = persons;
        this.firestations = firestations;
        this.medicalrecords = medicalRecords;
        dataLoaded = true;
    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    @PostConstruct
    public void loadModelFromDisk() {
        try {
            SafetyAlertsModel newModel = jsonHandler.jsonToModel(jsonDAO.readJsonFromFile(dataConfig.getDataFile()));
            this.persons = newModel.getPersons();
            this.firestations = newModel.getFirestations();
            this.medicalrecords = newModel.medicalrecords;
            dataLoaded = true;
        }
        catch (Exception e) {
            dataLoaded = false;
            logHandler.setLogger("SafetyAlertsModel");
            logHandler.error("Error saving database file " + e);
        }

    }

    @PreDestroy
    public void saveModelToDisk() {
        try {
            jsonDAO.writeJsonToFile(dataConfig.getDataFile(),jsonHandler.modelToJson(this));
        }
        catch (Exception e) {
            logHandler.setLogger("SafetyAlertsModel");
            logHandler.error("Error saving database file " + e);
        }
    }

    public void updateModel(SafetyAlertsModel model) {
        this.persons = model.getPersons();
        this.firestations = model.getFirestations();
        this.medicalrecords = model.getMedicalRecords();
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
