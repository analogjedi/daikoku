package com.primateer.daikoku.model;

public class NutrientGoal implements Goal {

	public final Nutrient.Type type;
	public final Amount minimum;
	public final Amount maximum;
	
	public NutrientGoal(Nutrient.Type type, Amount min, Amount max) {
		this.type = type;
		this.minimum = min;
		this.maximum = max;
	}
	
	@Override
	public Status match(Amount amount) {
		if (amount.compareTo(maximum) > 0) {
			return Goal.Status.FAILED;
		}
		if (amount.compareTo(minimum) < 0) {
			return Goal.Status.ACHIEVABLE;
		}
		return Goal.Status.MET;
	}
}
