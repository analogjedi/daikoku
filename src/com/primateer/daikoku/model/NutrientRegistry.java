package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutrientRegistry {

	private static NutrientRegistry instance;

	public static synchronized NutrientRegistry getInstance() {
		if (instance == null) {
			instance = new NutrientRegistry();
		}
		return instance;
	}

	private Map<String, Nutrient.Type> typeById = new HashMap<String, Nutrient.Type>();
	private List<Nutrient.Type> defaultNutrientTypes;
	private Amount defaultReferenceAmount;

	private NutrientRegistry() {
		defaultNutrientTypes = new ArrayList<Nutrient.Type>();
		defaultNutrientTypes.add(Nutrient.TYPE_ENERGY);
		defaultNutrientTypes.add(Nutrient.TYPE_PROTEIN);
		defaultNutrientTypes.add(Nutrient.TYPE_CARBS);
		defaultNutrientTypes.add(Nutrient.TYPE_FAT);
		
		defaultReferenceAmount = new Amount(100,Unit.UNIT_GRAM); // TODO
	}

	/**
	 * @param unit
	 * @return previously registered unit or {@code null}
	 */
	public Nutrient.Type register(Nutrient.Type type) {
		return typeById.put(type.id, type);
	}

	public Nutrient.Type getType(String symbol) {
		return typeById.get(symbol);
	}

	public List<Nutrient.Type> getDefaultNutritionFields() {
		return defaultNutrientTypes;
	}
	
	public Amount getDefaultReferenceAmount() {
		return defaultReferenceAmount;
	}
}
