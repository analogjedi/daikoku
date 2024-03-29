package com.primateer.daikoku.db;

import java.util.Date;

import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.vos.Meal;

public class DayDao {

	protected DayDao() {
	}

	public Day load(Date date) {
		Day vo = new Day(date);
		for (Meal meal : new MealDao().loadAll(date)) {
			vo.add(meal);
		}
		return vo;
	}
}
