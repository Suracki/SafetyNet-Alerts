package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.Firestation;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.stereotype.Service;

@Service
public class UpdateFirestation {

    public ResultModel addFirestation(ModelObjectFinder finder, SafetyAlertsModel model, Firestation firestation) {
        if (finder.findFirestation(firestation.getAddress(), model) == null) {
            model.addFirestation(firestation);
            return new ResultModel(model, true);
        }
        else {
            return new ResultModel(model, false);
        }
    }

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

