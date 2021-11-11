package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;

public class UpdateMedicalRecord {

    public ResultModel addMedicalRecord(ModelObjectFinder finder, SafetyAlertsModel model, MedicalRecord medicalRecord) {
        if (finder.findMedicalRecord(medicalRecord.getFirstName(),medicalRecord.getLastName(), model) == null) {
            model.addMedicalRecord(medicalRecord);
            return new ResultModel(model, true);
        }
        else {
            return new ResultModel(model, false);
        }
    }

    public ResultModel updateMedicalRecord(ModelObjectFinder finder, SafetyAlertsModel model, MedicalRecord newMedicalRecord) {
        MedicalRecord oldMedicalRecord = finder.findMedicalRecord(newMedicalRecord.getFirstName(), newMedicalRecord.getLastName(), model);
        if (oldMedicalRecord != null) {
            MedicalRecord[] medicalRecords = model.getMedicalRecords();
            for (MedicalRecord currentMedicalRecord : medicalRecords) {
                if (currentMedicalRecord.getFirstName().equals(newMedicalRecord.getFirstName())
                        && currentMedicalRecord.getLastName().equals(newMedicalRecord.getLastName())){
                    currentMedicalRecord.update(newMedicalRecord);
                }
            }
            model.setMedicalRecords(medicalRecords);

            return new ResultModel(model, true);
        }
        else {
            return new ResultModel(model, false);
        }
    }

    public ResultModel deleteMedicalRecord(ModelObjectFinder finder, SafetyAlertsModel model, MedicalRecord removeMedicalRecord) {
        //Confirm Medical Record exists
        if (finder.findMedicalRecord(removeMedicalRecord.getFirstName(),removeMedicalRecord.getLastName(), model) == null) {
            //No Medical Record found
            return new ResultModel(model, false);
        }
        //Update medical record in model
        MedicalRecord[] medicalRecords = model.getMedicalRecords();
        MedicalRecord[] updatedMedicalRecords = new MedicalRecord[medicalRecords.length - 1];
        int index = 0;
        for (MedicalRecord medicalRecord : medicalRecords){
            if (medicalRecord.getFirstName().equals(removeMedicalRecord.getFirstName())
                    && medicalRecord.getLastName().equals(removeMedicalRecord.getLastName())){
                //Do not copy, remove this medical record
            }
            else {
                updatedMedicalRecords[index] = medicalRecord;
                index++;
            }
        }
        model.setMedicalRecords(updatedMedicalRecords);
        //Return updated model
        return new ResultModel(model, true);
    }

}
