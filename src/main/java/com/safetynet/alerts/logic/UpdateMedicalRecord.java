package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.MedicalRecord;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.stereotype.Service;

/**
 * Service to make changes to MedicalRecord objects held in model
 */
@Service
public class UpdateMedicalRecord {

    /**
     * Add a MedicalRecord object to the model
     *
     * @param finder ModelObjectFinder used to parse collection
     * @param model SafetyAlertsModel
     * @param medicalRecord MedicalRecord object to be added to model
     * @return ResultModel containing updated SafetyAlertsModel and a boolean confirming if operation succeeded
     */
    public ResultModel addMedicalRecord(ModelObjectFinder finder, SafetyAlertsModel model, MedicalRecord medicalRecord) {
        if (finder.findMedicalRecord(medicalRecord.getFirstName(),medicalRecord.getLastName(), model) == null) {
            model.addMedicalRecord(medicalRecord);
            return new ResultModel(model, true);
        }
        else {
            return new ResultModel(model, false);
        }
    }

    /**
     * Update an existing MedicalRecord object, using firstname/lastname as identifiers
     *
     * @param finder ModelObjectFinder used to parse collection
     * @param model SafetyAlertsModel
     * @param newMedicalRecord updated MedicalRecord object
     * @return ResultModel containing updated SafetyAlertsModel and a boolean confirming if operation succeeded
     */
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

    /**
     * Remove a MedicalRecord from the model, using firstname/lastname as identifiers
     *
     * @param finder ModelObjectFinder used to parse collection
     * @param model SafetyAlertsModel
     * @param removeMedicalRecord MedicalRecord to be removed (variables other than firstname/lastname are not checked)
     * @return ResultModel containing updated SafetyAlertsModel and a boolean confirming if operation succeeded
     */
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
