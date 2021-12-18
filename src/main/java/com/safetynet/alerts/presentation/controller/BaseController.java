package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.logging.LogHandlerTiny;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    LogHandlerTiny logHandler;

}
