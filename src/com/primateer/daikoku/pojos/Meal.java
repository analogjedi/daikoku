package com.primateer.daikoku.pojos;

import java.util.Date;
import java.util.Map;

public class Meal {
	
	public static final int SCHEDULED = 0;
	public static final int RESERVED = 1;
	public static final int PREPARED = 2;
	public static final int CONSUMED = 3;

	String label;
	Recipe recipe;
	Map<String,Amount> additionalNutrients;
	int state;
	Date due;
}
