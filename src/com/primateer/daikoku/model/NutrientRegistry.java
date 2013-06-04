package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Scope;

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
		register(Nutrient.TYPE_OMEGA3);
		register(Nutrient.TYPE_OMEGA6);
		register(Nutrient.TYPE_FIBER);
		register(Nutrient.TYPE_POTASSIUM);
		register(Nutrient.TYPE_SUGAR);
		register(Nutrient.TYPE_VITAMIN_A);
		register(Nutrient.TYPE_VITAMIN_B1);
		register(Nutrient.TYPE_VITAMIN_B2);
		register(Nutrient.TYPE_VITAMIN_B3);
		register(Nutrient.TYPE_VITAMIN_B5);
		register(Nutrient.TYPE_VITAMIN_B6);
		register(Nutrient.TYPE_VITAMIN_B7);
		register(Nutrient.TYPE_VITAMIN_B9);
		register(Nutrient.TYPE_VITAMIN_B12);
		register(Nutrient.TYPE_VITAMIN_C);
		register(Nutrient.TYPE_VITAMIN_D);
		register(Nutrient.TYPE_VITAMIN_E);
		register(Nutrient.TYPE_VITAMIN_K);
		register(Nutrient.TYPE_CALCIUM);
		register(Nutrient.TYPE_IRON);
		register(Nutrient.TYPE_PHOSPHOR);
		register(Nutrient.TYPE_MAGNESIUM);
		register(Nutrient.TYPE_ZINC);
		register(Nutrient.TYPE_IODINE);
		register(Nutrient.TYPE_SELENIUM);
		register(Nutrient.TYPE_COPPER);
		register(Nutrient.TYPE_MANGANESE);
		register(Nutrient.TYPE_CHROMIUM);
		register(Nutrient.TYPE_MOLYBDENUM);
		register(Nutrient.TYPE_CHLORIDE);

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
		GoalSet goals = GoalRegistry.getInstance().getGoals(Scope.PER_DAY);
		if (goals.size() > 0) {
			List<Nutrient.Type> list = new ArrayList<Nutrient.Type>();
			for (Goal goal : goals) {
				list.add(goal.nutrientType);
			}
			return list;
		} else {
			return defaultNutrientTypes;
		}
	}

	public List<Nutrient.Type> getWatchList() {
		// TODO make configurable
		List<Goal> goals = GoalRegistry.getInstance().getGoals(Scope.PER_DAY);
		List<Nutrient.Type> types = new ArrayList<Nutrient.Type>();
		for (Goal goal : goals) {
			if (!types.contains(goal.nutrientType)) {
				types.add(goal.nutrientType);
			}
		}
		return types;
		// return getDefaultNutrientTypes();
	}

	public Amount getDefaultReferenceAmount() {
		return defaultReferenceAmount;
	}
}
