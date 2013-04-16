package com.primateer.daikoku.db;

import java.util.Date;
import java.util.List;

import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.vos.Meal;

public class DayDao extends Dao<Day> {

	public Day load(Date date) {
		Day vo = new Day(date);
		for (Meal meal : new MealDao().loadAll(date)) {
			vo.addMeal(meal);
		}
		return vo;
	}

	@Override
	public Day load(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Day> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long insert(Day vo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Day vo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Day vo) {
		// TODO Auto-generated method stub
		return 0;
	}

}
