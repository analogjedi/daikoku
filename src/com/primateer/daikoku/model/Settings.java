package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {

	
	private static Settings instance;
	
	public synchronized static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}


	private Amount defaultReferenceAmount = new Amount("100g");
	private String defaultMassUnit = "g";
	private String defaultEnergyUnit = "kcal";
	private Amount nullMassAmount;
	private Map<String,String> defaultNutritionFields;
	private List<String> allUnits;
	
	private Settings() {
		nullMassAmount = new Amount(0,getDefaultMassUnit());
		defaultNutritionFields = new HashMap<String,String>();
		defaultNutritionFields.put("E", defaultEnergyUnit);
		defaultNutritionFields.put("P", defaultMassUnit);
		defaultNutritionFields.put("C", defaultMassUnit);
		defaultNutritionFields.put("F", defaultMassUnit);
		
		allUnits = new ArrayList<String>();
		allUnits.add(defaultMassUnit);
		allUnits.add(defaultEnergyUnit);
	}

	
	public String getDefaultMassUnit() {
		return defaultMassUnit;
	}
	
	public String getDefaultEnergyUnit() {
		return defaultEnergyUnit;
	}
	
	public Amount getDefaultReferenceAmount() {
		return defaultReferenceAmount;
	}
	
	public Amount getNullMassAmount() {
		return nullMassAmount;
	}
	
	public Map<String,String> getDefaultNutritionFields() {
		return defaultNutritionFields;
	}


	public List<String> getAllUnits() {
		return allUnits;
	}
}
