package com.safetynet.alerts.logging;

import org.springframework.http.ResponseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LogHandlerLog4j implements LogHandler {

    private static Logger logger;

    public void setLogger(String name) {
        logger = LogManager.getLogger(name);
    }

    public void logRequest(String requestType, String mapping, String[] params) {
        logger.info(requestType + " request received on " + mapping + " with parameters " + stringArrayToString(params));
    }

    public void logResponse(String requestType, ResponseEntity<String> response) {
        if (response.getStatusCodeValue() == 200 || response.getStatusCodeValue() == 201) {
            logger.info(requestType + " request completed with response " + response.getStatusCodeValue());
            if(response.hasBody()){
                logger.info(requestType + " request response generated: " + formatLogResponse(response.getBody()));
            }
        }
        else {
            logger.error(requestType + " request completed with response " + response.getStatusCodeValue());
        }
    }

    public void error(String string) {
        logger.error(string);
    }

    public void debug(String debugClassName, String string) {
        org.tinylog.Logger.debug(debugClassName + ": " + string);
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
