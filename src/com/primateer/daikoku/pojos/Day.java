package com.primateer.daikoku.pojos;

import java.util.ArrayList;
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
		getMeals().add(meal);
	}
	
	public List<Meal> getMeals() {
		if (meals == null) {
			meals = new ArrayList<Meal>();
		}
		return meals;
	}

	public Object getTotalNutrition(String type) throws UnitConversionException {
		Amount total = Amount.NULL;
		if (meals != null) {
			for (Meal meal : meals) {
				Amount mealNutrition = meal.getTotalNutrition(type);
				if (mealNutrition != null) {
					total = mealNutrition.add(total);
				}
			}
		}
		return total;
	}
}
