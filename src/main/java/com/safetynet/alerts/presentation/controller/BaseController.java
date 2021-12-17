package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.logging.LogHandlerTiny;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    LogHandlerTiny logHandler;

    @Autowired
    SafetyAlertsModel safetyAlertsModel;

}
