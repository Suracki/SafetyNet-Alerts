package com.safetynet.alerts.logic.service;

import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.logic.parsers.ModelObjectFinder;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    @Autowired
    LogHandlerTiny logHandler;
    @Autowired
    SafetyAlertsModel model;
    @Autowired
    ModelObjectFinder finder;


}
