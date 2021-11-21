package com.safetynet.alerts.logging;

import org.springframework.http.ResponseEntity;
import org.tinylog.Logger;

public interface LogHandler {

    void setLogger(String name);

    void logRequest(String requestType, String mapping, String[] params);

    void logResponse(String requestType, ResponseEntity<String> response);

    void error(String string);

    void debug(String debugClassName, String string);

}
