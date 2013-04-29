package com.primateer.daikoku.model;

import com.primateer.daikoku.model.Amount.UnitConversionException;


public interface NutritionHolder {

	Amount getNutrition(Nutrient.Type type) throws UnitConversionException;
}
