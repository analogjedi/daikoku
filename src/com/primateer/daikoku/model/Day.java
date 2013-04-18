package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.vos.Meal;

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

	public Object getTotalNutrition(Nutrient.Type type)
			throws UnitConversionException {
		Amount total = new Amount(0, UnitRegistry.getInstance()
				.getDefaultUnitByType(Unit.TYPE_MASS));
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
