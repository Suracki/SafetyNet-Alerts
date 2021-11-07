package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.SafetyAlertsModel;

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
