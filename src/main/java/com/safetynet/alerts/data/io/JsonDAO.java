package com.safetynet.alerts.data.io;

import org.assertj.core.util.VisibleForTesting;

import java.io.*;

public class JsonDAO {

    public JsonDAO() {

    }

    public String readJsonFromFile(String filename) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            jsonBuilder.append(line).append('\n');
        }

        bufferedReader.close();

        return jsonBuilder.toString().trim();
    }

    public void writeJsonToFile(String filename, String json) throws IOException {

        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json);
        fileWriter.close();
    }


}
