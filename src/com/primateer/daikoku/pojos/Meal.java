package com.primateer.daikoku.pojos;

import java.util.Date;

public class Meal {
	
	public static final int SCHEDULED = 1;
	public static final int RESERVED = 2;
	public static final int PREPARED = 3;
	public static final int CONSUMED = 4;

	String label;
	Recipe recipe;
	Nutrition extraNutrition;
	Nutrition totalNutrition;
	int state;
	Date due;
}
