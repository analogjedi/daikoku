package com.primateer.daikoku.model;

public class NutrientMinGoal extends Goal {

	public final Amount minimum;

	public NutrientMinGoal(Scope scope, Nutrient.Type nutrientType, Amount min) {
		super(scope, nutrientType);
		this.minimum = min;
	}

	@Override
	public Status match(Amount amount) {
		if (amount.compareTo(minimum) < 0) {
			return Goal.Status.ACHIEVABLE;
		}
		return Goal.Status.MET;
	}

}
