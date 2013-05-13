package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.model.vos.Goal;

public class GoalSet extends ArrayList<Goal> {

	private Map<Nutrient.Type, Goal> minGoals = new HashMap<Nutrient.Type, Goal>();
	private Map<Nutrient.Type, Goal> maxGoals = new HashMap<Nutrient.Type, Goal>();
	
	public GoalSet(Collection<Goal> goals) {
		for (Goal goal : goals) {
			this.add(goal);
		}
	}

	@Override
	public void clear() {
		super.clear();
		minGoals.clear();
		maxGoals.clear();
	}

	@Override
	public boolean add(Goal goal) {
		if (super.contains(goal)) {
			return false;
		}

		super.add(goal);
		switch (goal.type) {
		case MINIMUM:
			minGoals.put(goal.nutrientType, goal);
			return true;
		case MAXIMUM:
			maxGoals.put(goal.nutrientType, goal);
			return true;
		default:
			throw new RuntimeException("Unknown goal type: " + goal.type);
		}
	}

	@Override
	public boolean remove(Object object) {
		if (super.remove(object)) {
			minGoals.remove(((Nutrient) object).type);
			maxGoals.remove(((Nutrient) object).type);
			return true;
		}
		return false;
	}
	
	public Goal.Status match(NutritionHolder nutrients) {
		Goal.Status result = Goal.Status.UNRATED;
		for (Goal goal : this) {
			Goal.Status match = goal.match(nutrients);
			result = result.ordinal() >= match.ordinal() ? result : match;
		}
		return result;
	}
	
	public Map<Goal,Goal.Status> matchAll(NutritionHolder nutrients) {
		Map<Goal,Goal.Status> results = new HashMap<Goal, Goal.Status>();
		for (Goal goal: this) {
			results.put(goal, goal.match(nutrients));
		}
		return results;
	}
}
