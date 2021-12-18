package com.safetynet.alerts.logic.updaters;

import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.stereotype.Service;

/**
 * Service to make changes to Firestation objects held in model
 */
@Service
public class UpdateFirestation {

    /**
     * Add a Firestation mapping object to the model
     *
     * @param finder ModelObjectFinder used to parse collection
     * @param model SafetyAlertsModel
     * @param firestation Firestation mapping object to be added to model
     * @return ResultModel containing updated SafetyAlertsModel and a boolean confirming if operation succeeded
     */
    public ResultModel addFirestation(ModelObjectFinder finder, SafetyAlertsModel model, Firestation firestation) {
        if (finder.findFirestation(firestation.getAddress(), model) == null) {
            model.addFirestation(firestation);
            return new ResultModel(model, true);
        }
        else {
            return new ResultModel(model, false);
        }
    }

    /**
     * Update an existing Firestation mapping object in the model
     * Allows the station number to be changed for an address
     *
     * @param finder ModelObjectFinder used to parse collection
     * @param model SafetyAlertsModel
     * @param newFirestation updated Firestation mapping
     * @return ResultModel containing updated SafetyAlertsModel and a boolean confirming if operation succeeded
     */
    public ResultModel updateFirestation(ModelObjectFinder finder, SafetyAlertsModel model, Firestation newFirestation) {
        Firestation oldFirestation = finder.findFirestation(newFirestation.getAddress(), model);
        if (oldFirestation != null) {
            Firestation[] firestations = model.getFirestations();
            for (Firestation currentFirestation : firestations) {
                if (currentFirestation.getAddress().equals(newFirestation.getAddress())){
                    currentFirestation.update(newFirestation);
                }
            }
            model.setFirestations(firestations);

            return new ResultModel(model, true);
        }
        else {
            return new ResultModel(model, false);
        }
    }

    /**
     * Remove a Firestation mapping from the model
     *
     * @param finder ModelObjectFinder used to parse collection
     * @param model SafetyAlertsModel
     * @param removeFirestation Firestation mapping to be found & removed
     * @return
     */
    public ResultModel deleteFirestation(ModelObjectFinder finder, SafetyAlertsModel model, Firestation removeFirestation) {
        //Confirm firestation mapping exists
        if (finder.findFirestation(removeFirestation.getAddress(), model) == null) {
            //No Firestation mapping found for this address
            return new ResultModel(model, false);
        }
        //Update firestation mappings in model
        Firestation[] firestations = model.getFirestations();
        Firestation[] updatedFirestations = new Firestation[firestations.length - 1];
        int index = 0;
        for (Firestation firestation : firestations){
            if (firestation.getAddress().equals(removeFirestation.getAddress())){
                //Do not copy, remove this firestation mapping
            }
            else {
                updatedFirestations[index] = firestation;
                index++;
            }
        }
        model.setFirestations(updatedFirestations);
        //Return updated model
        return new ResultModel(model, true);
    }

}

