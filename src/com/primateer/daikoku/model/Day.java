package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.vos.Meal;

public class Day implements NutritionHolder {

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

	@Override
	public Amount getNutrition(Nutrient.Type type)
			throws UnitConversionException {
		Amount total = new Amount(0, type.defaultUnit);
		if (meals != null) {
			for (Meal meal : meals) {
				Amount mealNutrition = meal.getNutrition(type);
				if (mealNutrition != null) {
					total = mealNutrition.add(total);
				}
			}
		}
		return total;
	}
}
