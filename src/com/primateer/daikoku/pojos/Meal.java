package com.primateer.daikoku.pojos;

import java.util.Date;
import java.util.Map;

public class Meal {
	
	public static final int SCHEDULED = 1;
	public static final int RESERVED = 2;
	public static final int PREPARED = 3;
	public static final int CONSUMED = 4;

	String label;
	Recipe recipe;
	Map<String,Amount> additionalNutrients;
	int state;
	Date due;
}
