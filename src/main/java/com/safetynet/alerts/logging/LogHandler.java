package com.safetynet.alerts.logging;

import org.springframework.http.ResponseEntity;

/**
 * Interface for LogHandler
 *
 * Allows implementation of different LogHandler classes for various logging frameworks
 * without requiring large amounts of changes to logged code elsewhere in project
 */
public interface LogHandler {

    void setLogger(String name);

    void logRequest(String requestType, String mapping, String[] params);

    void logResponse(String requestType, ResponseEntity<String> response);

    void error(String string);

    void debug(String debugClassName, String string);

}
