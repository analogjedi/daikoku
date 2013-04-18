package com.primateer.daikoku.model.vos;

import java.util.Date;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.ValueObject;

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

	public Amount getTotalNutrition(Nutrient.Type type)
			throws UnitConversionException {
		Amount total = new Amount(0, UnitRegistry.getInstance()
				.getDefaultUnitByType(Unit.TYPE_MASS));
		if (extraNutrition != null) {
			total = extraNutrition.getNutrients().get(type).amount;
		}
		if (recipe != null) {
			total = total.add(recipe.getTotalNutrition(type));
		}
		return total;
	}
}
