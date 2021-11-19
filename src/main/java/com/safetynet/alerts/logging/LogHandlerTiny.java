package com.safetynet.alerts.logging;

import org.springframework.http.ResponseEntity;
import org.tinylog.Logger;
import org.springframework.stereotype.Service;

@Service
public class LogHandlerTiny implements LogHandler{

    private static String className;

    public LogHandlerTiny() {
    }

    public void setLogger(String name) {
        className = name;
    }

    public void logRequest(String requestType, String mapping, String[] params) {
        Logger.info(className + ": " + requestType + " request received on " + mapping + " with parameters " + stringArrayToString(params));
    }

    public void logResponse(String requestType, ResponseEntity<String> response) {
        if (response.getStatusCodeValue() == 200 || response.getStatusCodeValue() == 201) {
            Logger.info(className + ": " + requestType + " request completed with response " + response.getStatusCodeValue());
            if(response.hasBody()){
                Logger.info(className + ": " + requestType + " request reponse generated: " + formatLogResponse(response.getBody()));
            }
        }
        else {
            Logger.error(className + ": " + requestType + " request completed with response " + response.getStatusCodeValue());
        }
    }

    public void error(String string) {
        Logger.error(string);
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
