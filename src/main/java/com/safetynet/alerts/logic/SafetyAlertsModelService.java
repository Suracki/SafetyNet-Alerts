package com.safetynet.alerts.logic;

import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SafetyAlertsModelService {

    SafetyAlertsModel safetyAlertsModel;
    ModelObjectFinder modelObjectFinder;
    UpdatePerson updatePerson;

    @Autowired
    public SafetyAlertsModelService(SafetyAlertsModel safetyAlertsModel, ModelObjectFinder modelObjectFinder, UpdatePerson updatePerson) {
        this.safetyAlertsModel = safetyAlertsModel;
        this.modelObjectFinder = modelObjectFinder;
        this.updatePerson = updatePerson;
    }

}
