package com.safetynet.alerts.logging;

import org.springframework.http.ResponseEntity;
import org.tinylog.Logger;
import org.springframework.stereotype.Service;

/**
 * LogHandler class for using Tinylog logging framework
 */
@Service
public class LogHandlerTiny implements LogHandler{

    private static String className;

    /**
     * Set the name of the class being logged
     *
     * @param name class name as String
     */
    public void setLogger(String name) {
        className = name;
    }

    /**
     * Log a request to an endpoint
     *
     * @param requestType type of request, eg GET/POST/PUT/DELETE
     * @param mapping endpoint mapping, eg /firestation
     * @param params collection of parameters received to endpoint
     */
    public void logRequest(String requestType, String mapping, String[] params) {
        Logger.info(className + ": " + requestType + " request received on " + mapping + " with parameters " + stringArrayToString(params));
    }

    /**
     * Log a response from an endpoint
     *
     * @param requestType type of request, eg GET/POST/PUT/DELETE
     * @param response the response sent by the endpoint
     */
    public void logResponse(String requestType, ResponseEntity<String> response) {
        if (response.getStatusCodeValue() == 200 || response.getStatusCodeValue() == 201) {
            Logger.info(className + ": " + requestType + " request completed with response " + response.getStatusCodeValue());
            if(response.hasBody()){
                Logger.info(className + ": " + requestType + " request response generated: " + formatLogResponse(response.getBody()));
            }
        }
        else {
            Logger.error(className + ": " + requestType + " request completed with response " + response.getStatusCodeValue());
        }
    }

    /**
     * Log an error
     *
     * @param string the error to be logged
     */
    public void error(String string) {
        Logger.error(className + ": " + string);
    }

    /**
     * Log a step for debugging
     *
     * @param debugClassName the name of the class the step occurred in
     * @param string the details to be logged
     */
    public void debug(String debugClassName, String string) {
        Logger.debug(debugClassName + ": " + string);
    }

    private String formatLogResponse(String response) {
        return response.replace("\n", "").replace("  ", "");
    }
    private String stringArrayToString(String[] stringArray) {
        StringBuilder builder = new StringBuilder();
        if (stringArray.length == 0) {
            return "[]";
        }
        builder.append("[");
        for (String string : stringArray) {
            builder.append("\"")
                    .append(string)
                    .append("\",");
        }
        //remove final ,
        builder.setLength(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

}
