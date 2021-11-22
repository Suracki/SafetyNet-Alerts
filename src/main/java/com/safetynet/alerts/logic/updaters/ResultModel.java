package com.safetynet.alerts.logic.updaters;

import com.safetynet.alerts.presentation.model.SafetyAlertsModel;

/**
 * Object used to hold an updated SafetyAlertsModel along with a boolean confirming if an operation was successful
 */
public class ResultModel {

    private SafetyAlertsModel model;
    private Boolean bool;

    public ResultModel(SafetyAlertsModel model, Boolean bool){
        this.model = model;
        this.bool = bool;
    }

    public SafetyAlertsModel getModel() {
        return model;
    }

    public Boolean getBool() {
        return bool;
    }
}
