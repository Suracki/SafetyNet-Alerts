package com.safetynet.alerts.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHandler {

	private Gson gson;
	
	public JsonHandler () {
	}

	public void loadData() {
		gson = new Gson();
		//JsonReader reader = new JsonReader(new FileReader(filename));



	}

	public String saveData(Person[] persons, Firestation[] firestations, MedicalRecord[] medicalRecords) {

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		StringBuilder stringBuilder = new StringBuilder();
		String jsonString;

		//Add persons
		stringBuilder.append("{\n    \"persons\": [\n");
		for (int i = 0; i < persons.length; i++){
			stringBuilder.append("        " + gson.toJson(persons[i]) + ",\n");
		}
		stringBuilder.append("    ],\n");

		//Add firestations
		stringBuilder.append("    \"firestations\": [\n");
		for (int i = 0; i < firestations.length; i++){
			stringBuilder.append("        " + gson.toJson(firestations[i]) + ",\n");
		}
		stringBuilder.append("    ],\n");

		//Add medicalRecords
		stringBuilder.append("    \"medicalrecords\": [\n");
		for (int i = 0; i < medicalRecords.length; i++){
			stringBuilder.append("        " + gson.toJson(medicalRecords[i]) + ",\n");
		}
		stringBuilder.append("    ],\n}");

		jsonString = stringBuilder.toString();
		System.out.println("Json starts:");
		System.out.println(jsonString);
		System.out.println("Json ends.");

		return jsonString;

	}
	
	
	
}
