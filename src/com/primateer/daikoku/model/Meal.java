package com.primateer.daikoku.model;

import java.util.Date;

import com.primateer.daikoku.pojos.Amount;
import com.primateer.daikoku.pojos.Amount.UnitConversionException;

public class Meal {

	public static final int SCHEDULED = 1;
	public static final int RESERVED = 2;
	public static final int PREPARED = 3;
	public static final int CONSUMED = 4;

	private long id = -1;
	private String label;
	private Recipe recipe;
	private Date due;
	private int state;
	private Nutrition extraNutrition;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public Date getDue() {
		return due;
	}

	public void setDue(Date due) {
		this.due = due;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Nutrition getExtraNutrition() {
		return extraNutrition;
	}

	public Amount getTotalNutrition(String type) throws UnitConversionException {
		Amount total = Amount.NULL;
		if (extraNutrition != null) {
			total = extraNutrition.getNutrients().get(type);
		}
		if (recipe != null) {
			total = total.add(recipe.getTotalNutrition(type));
		}
		return total;
	}
}
