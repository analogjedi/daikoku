package com.primateer.daikoku.model.vos;

import java.util.Date;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Settings;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.model.Amount.UnitConversionException;

public class Meal extends ValueObject<Meal> {

	public static final int SCHEDULED = 1;
	public static final int RESERVED = 2;
	public static final int PREPARED = 3;
	public static final int CONSUMED = 4;

	private String label;
	private Recipe recipe;
	private Date due;
	private int state;
	private Nutrition extraNutrition;

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
		Amount total = Settings.getInstance().getNullMassAmount();
		if (extraNutrition != null) {
			total = extraNutrition.getNutrients().get(type);
		}
		if (recipe != null) {
			total = total.add(recipe.getTotalNutrition(type));
		}
		return total;
	}
}
