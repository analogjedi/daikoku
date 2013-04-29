package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.Map;

public class GoalRegistry {

	private static GoalRegistry instance;

	public static final GoalRegistry getInstance() {
		if (instance == null) {
			instance = new GoalRegistry();
		}
		return instance;
	}

	private GoalRegistry() {
	}

	public Map<Nutrient.Type, Goal> getGoals(Goal.Scope scope) {
		// TODO just test data for now
		Map<Nutrient.Type, Goal> list = new HashMap<Nutrient.Type, Goal>();
		list.put(Nutrient.TYPE_ENERGY, new NutrientGoal(Nutrient.TYPE_ENERGY,
				new Amount("1800kcal"), new Amount("2100kcal")));
		list.put(Nutrient.TYPE_PROTEIN, new NutrientGoal(Nutrient.TYPE_PROTEIN,
				new Amount("100g"), new Amount("200g")));
		list.put(Nutrient.TYPE_CARBS, new NutrientGoal(Nutrient.TYPE_CARBS,
				new Amount("0g"), new Amount("100g")));
		return list;
	}
}
