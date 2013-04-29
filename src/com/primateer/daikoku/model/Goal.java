package com.primateer.daikoku.model;

import com.primateer.daikoku.Application;

public interface Goal {
	
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

	Status match(Amount amount);
}
