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
	private List<Nutrient.Type> defaultNutrientTypes = new ArrayList<Nutrient.Type>();
	private List<Nutrient.Type> allNutrientTypes = new ArrayList<Nutrient.Type>();
	private Amount defaultReferenceAmount;

	private NutrientRegistry() {
		defaultNutrientTypes.add(Nutrient.TYPE_ENERGY);
		defaultNutrientTypes.add(Nutrient.TYPE_PROTEIN);
		defaultNutrientTypes.add(Nutrient.TYPE_CARBS);
		defaultNutrientTypes.add(Nutrient.TYPE_FAT);

		for (Nutrient.Type type : defaultNutrientTypes) {
			register(type);
		}
		register(Nutrient.TYPE_CHOLESTEROL);
		register(Nutrient.TYPE_SODIUM);
		register(Nutrient.TYPE_SATURATED_FAT);
		register(Nutrient.TYPE_FIBER);
		register(Nutrient.TYPE_SUGAR);

		defaultReferenceAmount = new Amount(100, Unit.UNIT_GRAM); // TODO
	}

	/**
	 * @param unit
	 * @return previously registered unit or {@code null}
	 */
	public Nutrient.Type register(Nutrient.Type type) {
		allNutrientTypes.add(type);
		return typeById.put(type.id, type);
	}

	public Nutrient.Type getType(String symbol) {
		return typeById.get(symbol);
	}

	public List<Nutrient.Type> getAllNutrientTypes() {
		return allNutrientTypes;
	}

	public List<Nutrient.Type> getDefaultNutrientTypes() {
		return defaultNutrientTypes;
	}

	public Amount getDefaultReferenceAmount() {
		return defaultReferenceAmount;
	}
}
