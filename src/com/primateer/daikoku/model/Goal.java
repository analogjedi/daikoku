package com.primateer.daikoku.model;

import com.primateer.daikoku.Application;

public abstract class Goal {
	
	public enum Scope {
		PER_MEAL, PER_DAY, PER_WEEK, PER_MONTH;
	}

	public enum Status {
		UNRATED(Application.TEXTCOLOR_GREY), MET(
				Application.TEXTCOLOR_GREEN), ACHIEVABLE(
				Application.TEXTCOLOR_BLUE), FAILED(Application.TEXTCOLOR_RED);

		public final int color;

		Status(int color) {
			this.color = color;
		}
	}
	
	public final Scope scope;
	public final Nutrient.Type nutrientType;
	
	public Goal(Scope scope, Nutrient.Type nutrientType) {
		this.scope = scope;
		this.nutrientType = nutrientType;
	}

	public abstract Status match(Amount amount);
}
