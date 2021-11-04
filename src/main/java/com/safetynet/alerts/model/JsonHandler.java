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

	public String convertToJson(Person[] persons, Firestation[] firestations, MedicalRecord[] medicalRecords) {

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		StringBuilder stringBuilder = new StringBuilder();
		String jsonString;

		//Add persons
		stringBuilder.append("{\n    \"persons\": [\n");
		for (Person person : persons){
			stringBuilder.append("        ").append(gson.toJson(person)).append(",\n");
		}
		stringBuilder.append("    ],\n");

		//Add firestations
		stringBuilder.append("    \"firestations\": [\n");
		for (Firestation firestation : firestations){
			stringBuilder.append("        ").append(gson.toJson(firestation)).append(",\n");
		}
		stringBuilder.append("    ],\n");

		//Add medicalRecords
		stringBuilder.append("    \"medicalrecords\": [\n");
		for (MedicalRecord medicalRecord : medicalRecords){
			stringBuilder.append("        ").append(gson.toJson(medicalRecord)).append(",\n");
		}
		stringBuilder.append("    ],\n}");

		jsonString = stringBuilder.toString();
		//TODO remove testing comments
		System.out.println("Json starts:");
		System.out.println(jsonString);
		System.out.println("Json ends.");

		return jsonString;

	}
	
	
	
}
