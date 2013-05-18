package com.primateer.daikoku.model;

import com.primateer.daikoku.model.Amount.AmountException;


public interface NutritionHolder {

	Amount getNutrition(Nutrient.Type type) throws AmountException;
}
