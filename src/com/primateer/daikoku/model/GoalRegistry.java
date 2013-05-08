package com.primateer.daikoku.model;

import java.util.List;

import com.primateer.daikoku.db.GoalDao;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Scope;

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
		return new GoalDao().loadAll(Scope.PER_DAY);
	}
}
