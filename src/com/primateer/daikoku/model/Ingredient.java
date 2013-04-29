package com.primateer.daikoku.model;

public interface Ingredient extends NutritionHolder {

	double getUnits();

	Amount getDefaultAmount();
	
	Amount getAmountPerUnit();
}
