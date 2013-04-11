package com.primateer.daikoku.db;

import java.util.Date;

import com.primateer.daikoku.model.Meal;
import com.primateer.daikoku.pojos.Day;

public class DayDao extends Dao {

	public Day load(Date date) {
		Day vo = new Day(date);
		for (Meal meal : new MealDao().loadAll(date)) {
			vo.addMeal(meal);
		}
		return vo;
	}
}
