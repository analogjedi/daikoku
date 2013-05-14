package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Type;

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
		if (max != null) {
			int maxPos = this.indexOf(max);
			max = new Goal(Type.MINIMUM, max.scope, max.nutrientType,
					max.amount);
			this.set(maxPos, max);
			minGoals.put(type, max);
		} else {
			minGoals.remove(type);
		}
		if (min != null) {
			int minPos = this.indexOf(min);
			min = new Goal(Type.MAXIMUM, min.scope, min.nutrientType,
					min.amount);
			this.set(minPos, min);
			maxGoals.put(type, min);
		} else {
			maxGoals.remove(type);
		}
	}

	@Override
	public Goal set(int pos, Goal goal) {
		getRegistry(goal).put(goal.nutrientType, goal);
		return super.set(pos, goal);
	}

	public Goal getFreeGoal(Nutrient.Type type, Goal.Scope scope) {
		if (minGoals.get(type) == null) {
			return new Goal(Goal.Type.MINIMUM, scope, type,
					type.getNullAmount());
		}
		if (maxGoals.get(type) == null) {
			return new Goal(Goal.Type.MAXIMUM, scope, type,
					type.getNullAmount());
		}
		return null;
	}

	private Map<Nutrient.Type, Goal> getRegistry(Goal goal) {
		switch (goal.type) {
		case MINIMUM:
			return minGoals;
		case MAXIMUM:
			return maxGoals;
		default:
			throw new RuntimeException("Unknown goal type: " + goal.type);
		}
	}

	@Override
	public boolean add(Goal goal) {
		if (goal == null || super.contains(goal)) {
			return false;
		}
		super.add(goal);
		getRegistry(goal).put(goal.nutrientType, goal);
		return true;
	}

	@Override
	public boolean remove(Object object) {
		if (super.remove(object)) {
			Goal goal = (Goal) object;
			getRegistry(goal).remove(goal.nutrientType);
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
