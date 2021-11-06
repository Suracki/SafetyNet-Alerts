package com.safetynet.alerts.presentation.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHandler {

	private Gson gson;
	
	public JsonHandler () {
	}

	public String modelToJson(SafetyAlertsModel model) {

		//Create Gson & set up variables
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		StringBuilder stringBuilder = new StringBuilder();
		String jsonString;

		//Add persons to stringbuilder as json
		stringBuilder.append("{\n    \"persons\": [\n");
		for (Person person : model.getPersons()){
			stringBuilder.append("        ").append(gson.toJson(person)).append(",\n");
		}
		stringBuilder.append("    ],\n");

		//Add firestations to stringbuilder as json
		stringBuilder.append("    \"firestations\": [\n");
		for (Firestation firestation : model.getFirestations()){
			stringBuilder.append("        ").append(gson.toJson(firestation)).append(",\n");
		}
		stringBuilder.append("    ],\n");

		//Add medicalRecords to stringbuilder as json
		stringBuilder.append("    \"medicalrecords\": [\n");
		for (MedicalRecord medicalRecord : model.getMedicalRecords()){
			stringBuilder.append("        ").append(gson.toJson(medicalRecord)).append(",\n");
		}
		stringBuilder.append("    ],\n}");

		//Convert stringbuilder to json string
		jsonString = stringBuilder.toString();

		return jsonString;

	}

	public SafetyAlertsModel jsonToModel(String jsonString) {

		//Create Gson & set up variables
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		SafetyAlertsModel model = new SafetyAlertsModel();

		//Split up json string into arrays of each class type
		String[][] jsonsByClass = splitJsonByClass(jsonString);
		String[] personsString = jsonsByClass[0];
		String[] firestationsString = jsonsByClass[1];
		String[] medicalRecordsString = jsonsByClass[2];


		//Create objects for each line in json
		for (String line : personsString) {
			model.addPerson(gson.fromJson(removeLastChar(line), Person.class));
		}
		for (String line : firestationsString) {
			model.addFirestation(gson.fromJson(removeLastChar(line), Firestation.class));
		}
		for (String line : medicalRecordsString) {
			model.addMedicalRecord(gson.fromJson(removeLastChar(line), MedicalRecord.class));
		}

		return model;

	}

	private String removeLastChar(String s) {
		return (s == null || s.length() == 0)
				? null
				: (s.substring(0, s.length() - 1));
	}

	private String[][] splitJsonByClass(String jsonString) {

		String[] jsonArray = jsonString.split("\n");
		String[] persons = new String[0];
		String[] firestations = new String[0];
		String[] medicalrecords = new String[0];

		boolean personBool = false;
		boolean firestationBool = false;
		boolean medicalRecordBool = false;

		for (String line : jsonArray) {
			if (line.replaceAll("\\s","").equals("\"persons\":[")) {
				personBool = true; firestationBool = false;	medicalRecordBool = false;
			}
			else if (line.replaceAll("\\s","").equals("\"firestations\":[")) {
				personBool = false;	firestationBool = true; medicalRecordBool = false;
			}
			else if (line.replaceAll("\\s","").equals("\"medicalrecords\":[")) {
				personBool = false; firestationBool = false; medicalRecordBool = true;
			}
			else if (!line.replaceAll("\\s","").equals("{") &&
					!line.replaceAll("\\s","").equals("],") &&
					!line.replaceAll("\\s","").equals("}")) {
				if (personBool) {
					persons = addElement(persons, line);
				}
				else if (firestationBool) {
					firestations = addElement(firestations, line);
				}
				else if (medicalRecordBool) {
					medicalrecords = addElement(medicalrecords, line);
				}
			}
		}
		return new String[][] {persons, firestations, medicalrecords};

	}

	private String[] addElement(String[] array, String element) {
		String[] updatedArray = new String[array.length + 1];
		System.arraycopy(array, 0, updatedArray, 0,array.length);
		updatedArray[array.length] = element;
		return updatedArray;
	}
}
