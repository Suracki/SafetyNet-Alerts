package com.safetynet.alerts.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.safetynet.alerts.presentation.model.SafetyAlertsModel;
import org.springframework.stereotype.Service;

/**
 * JsonHandler converts SafetyAlertsModel objects to the desired Json format and vice versa
 */
@Service
public class JsonHandler {

	/**
	 * Convert a SafetyAlertsModel object into a Json string
	 *
	 * @param model SafetyAlertsModel object
	 * @return Json string
	 */
	public String modelToJson(SafetyAlertsModel model) {

		//Create Gson & set up variables
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();

		//Create Json String from model object via Gson
		String jsonString = gson.toJson(model);

		return jsonString;

	}

	/**
	 * Create a SafetyAlertsModel object from a Json string
	 *
	 * @param jsonString
	 * @return crated SafetyAlertsModel object
	 */
	public SafetyAlertsModel jsonToModel(String jsonString) {

		//Create Gson & set up variables
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		//Create model from Json string using Gson
		return gson.fromJson(jsonString, SafetyAlertsModel.class);

	}
}
