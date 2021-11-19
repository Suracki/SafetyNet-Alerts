package com.safetynet.alerts.logging;

import org.springframework.http.ResponseEntity;

public interface LogHandler {

    public void setLogger(String name);

    public void logRequest(String requestType, String mapping, String[] params);

    public void logResponse(String requestType, ResponseEntity<String> response);

    public void error(String string);

}
