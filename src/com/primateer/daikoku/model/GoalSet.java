package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.model.vos.Goal;

public class GoalSet extends ArrayList<Goal> {

	private Map<Nutrient.Type, Goal> minGoals = new HashMap<Nutrient.Type, Goal>();
	private Map<Nutrient.Type, Goal> maxGoals = new HashMap<Nutrient.Type, Goal>();

	public GoalSet() {
		super();
	}

	public GoalSet(Collection<Goal> goals) {
		super();
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
	
	public Collection<Nutrient.Type> getOccupiedTypes() {
		Collection<Nutrient.Type> results = minGoals.keySet();
		results.retainAll(maxGoals.keySet());
		return results;
	}
	
	public void swapMinMax(Nutrient.Type type) {
		Goal min = minGoals.get(type);
		Goal max = maxGoals.get(type);
		minGoals.put(type, min);
		maxGoals.put(type, max);
	}

	public Goal getFreeGoal(Nutrient.Type type, Goal.Scope scope) {
		if (!minGoals.containsKey(type)) {
			return new Goal(Goal.Type.MINIMUM, scope, type,
					type.getNullAmount());
		}
		if (!maxGoals.containsKey(type)) {
			return new Goal(Goal.Type.MAXIMUM, scope, type,
					type.getNullAmount());
		}
		return null;
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
			Goal goal = (Goal) object;
			switch (goal.type) {
			case MINIMUM:
				minGoals.remove(goal.nutrientType);
				return true;
			case MAXIMUM:
				maxGoals.remove(goal.nutrientType);
				return true;
			default:
				throw new RuntimeException("Unknown goal type: " + goal.type);
			}
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

	public Map<Goal, Goal.Status> matchAll(NutritionHolder nutrients) {
		Map<Goal, Goal.Status> results = new HashMap<Goal, Goal.Status>();
		for (Goal goal : this) {
			results.put(goal, goal.match(nutrients));
		}
		return results;
	}
}
