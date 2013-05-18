package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.Date;

import com.primateer.daikoku.model.Amount.AmountException;
import com.primateer.daikoku.model.vos.Meal;

public class Day extends ArrayList<Meal> implements NutritionHolder {

	public final Date date;

	public Day(Date date) {
		this.date = date;
	}

	@Override
	public Amount getNutrition(Nutrient.Type type)
			throws AmountException {
		Amount total = type.getNullAmount();
		for (Meal meal : this) {
			Amount mealNutrition = meal.getNutrition(type);
			if (mealNutrition != null) {
				total = mealNutrition.add(total);
			}
		}
		return total;
	}
}
