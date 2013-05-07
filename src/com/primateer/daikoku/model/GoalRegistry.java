package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.List;

import com.primateer.daikoku.model.vos.Goal;

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

	public List<Goal> getGoals(Goal.Scope scope) {
		// TODO just test data for now
		List<Goal> list = new ArrayList<Goal>();
		list.add(new Goal(Goal.Type.MINIMUM, scope, Nutrient.TYPE_ENERGY,
				new Amount("1800kcal")));
		list.add(new Goal(Goal.Type.MAXIMUM, scope, Nutrient.TYPE_ENERGY,
				new Amount("2100kcal")));
		list.add(new Goal(Goal.Type.MINIMUM, scope, Nutrient.TYPE_PROTEIN,
				new Amount("100g")));
		list.add(new Goal(Goal.Type.MAXIMUM, scope, Nutrient.TYPE_CARBS,
				new Amount("100g")));
		return list;
	}
}
