package com.safetynet.alerts.data.io;

import org.springframework.stereotype.Service;

import java.io.*;

/**
 * JsonDAO is the Data Access Object for reading/writing Json files
 */
@Service
public class JsonDAO {

    /**
     * readJsonFromFile reads a .json file and returns its contents as a trimmed String
     *
     * @param filename the location of the data file
     * @return the contents of the Json file as a trimmed String
     * @throws IOException
     */
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

    /**
     * writeJsonToFile takes a Json string and writes it to a file
     *
     * @param filename the location of the data file
     * @param json the String to be written to the file
     * @throws IOException
     */
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
