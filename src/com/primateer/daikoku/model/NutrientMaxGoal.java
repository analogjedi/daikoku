package com.primateer.daikoku.model;

public class NutrientMaxGoal extends Goal {

	public final Amount maximum;

	public NutrientMaxGoal(Scope scope, Nutrient.Type nutrientType, Amount max) {
		super(scope, nutrientType);
		this.maximum = max;
	}

	@Override
	public Status match(Amount amount) {
		if (amount.compareTo(maximum) > 0) {
			return Goal.Status.FAILED;
		}
		return Goal.Status.MET;
	}

}
