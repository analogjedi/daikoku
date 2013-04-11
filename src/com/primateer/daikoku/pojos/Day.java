package com.primateer.daikoku.pojos;

import java.util.Date;
import java.util.List;

import com.primateer.daikoku.model.Meal;
import com.primateer.daikoku.pojos.Amount.UnitConversionException;

public class Day {

	public final Date date;
	private List<Meal> meals;

	public Day(Date date) {
		this.date = date;
	}

	public void addMeal(Meal meal) {
		meals.add(meal);
	}

	public Object getTotalNutrition(String type) throws UnitConversionException {
		Amount total = null;
		if (meals != null) {
			for (Meal meal : meals) {
				Amount mealNutrition = meal.getTotalNutrition(type);
				total = mealNutrition.add(total);
			}
		}
		return total;
	}
}
