package com.primateer.daikoku.model;

import java.util.HashMap;
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
	private Unit defaultMassUnit = Unit.UNIT_GRAM;
	private Unit defaultEnergyUnit = Unit.UNIT_KILOCALORIE;
	private Amount nullMassAmount;
	private Map<Nutrient.Type,Unit> defaultNutritionFields;
	
	private Settings() {
		nullMassAmount = new Amount(0,getDefaultMassUnit());
		defaultNutritionFields = new HashMap<Nutrient.Type,Unit>();
		defaultNutritionFields.put(Nutrient.TYPE_ENERGY, defaultEnergyUnit);
		defaultNutritionFields.put(Nutrient.TYPE_PROTEIN, defaultMassUnit);
		defaultNutritionFields.put(Nutrient.TYPE_CARBS, defaultMassUnit);
		defaultNutritionFields.put(Nutrient.TYPE_FAT, defaultMassUnit);
	}

	
	public Unit getDefaultMassUnit() {
		return defaultMassUnit;
	}
	
	public Unit getDefaultEnergyUnit() {
		return defaultEnergyUnit;
	}
	
	public Amount getDefaultReferenceAmount() {
		return defaultReferenceAmount;
	}
	
	public Amount getNullMassAmount() {
		return nullMassAmount;
	}
	
	public Map<Nutrient.Type,Unit> getDefaultNutritionFields() {
		return defaultNutritionFields;
	}
}
