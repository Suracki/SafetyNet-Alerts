package com.safetynet.alerts.logic.updaters;

import com.safetynet.alerts.presentation.model.SafetyAlertsModel;

/**
 * Object used to hold an updated SafetyAlertsModel along with a boolean confirming if an operation was successful
 */
public class ResultModel {

    private SafetyAlertsModel model;
    private Boolean operationSuccessful;

    public ResultModel(SafetyAlertsModel model, Boolean operationSuccessful){
        this.model = model;
        this.operationSuccessful = operationSuccessful;
    }

    public SafetyAlertsModel getModel() {
        return model;
    }

    public Boolean successful() {
        return operationSuccessful;
    }
}
