package com.primateer.daikoku.model;

import java.util.Date;

import com.primateer.daikoku.db.DayDao;
import com.primateer.daikoku.db.MealDao;
import com.primateer.daikoku.pojos.Day;

public class Model {

	private static Model instance;
	
	public static Model getInstance() {
		if (instance == null) {
			instance = new Model();
		}
		return instance;
	}
	
	public Day getDay(Date date) {
		return new DayDao().load(date);
	}
	
	public long register(Meal meal) {
		return new MealDao().insert(meal);
	}
}
