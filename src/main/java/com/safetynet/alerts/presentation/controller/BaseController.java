package com.safetynet.alerts.presentation.controller;

import com.safetynet.alerts.logging.LogHandlerTiny;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class BaseController {

    @Autowired
    LogHandlerTiny logHandler;

    public String stringArrayToString(String[] stringArray) {
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

    public String[] intArrayToStringArray(int[] intArray) {
        return Arrays.stream(intArray).mapToObj(String::valueOf).toArray(String[]::new);
    }


}
