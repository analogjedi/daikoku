package com.primateer.daikoku.model;

import com.primateer.daikoku.db.DBController;
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

	public GoalSet getGoals(Goal.Scope scope) {
		return new GoalSet(DBController.getInstance().loadAllGoals(scope));
	}
}
