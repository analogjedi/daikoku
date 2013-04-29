package com.primateer.daikoku.model.vos;

import java.util.Date;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutritionHolder;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.ValueObject;

public class Meal extends ValueObject<Meal> implements NutritionHolder {

	public enum State {
		SCHEDULED(R.string.meal_state_scheduled,
				Application.ICON_STATE_SCHEDULED), RESERVED(
				R.string.meal_state_reserved, Application.ICON_STATE_RESERVED), PREPARED(
				R.string.meal_state_prepared, Application.ICON_STATE_PREPARED), CONSUMED(
				R.string.meal_state_consumed, Application.ICON_STATE_CONSUMED);

		public final int string;
		public final int icon;

		private State(int string, int icon) {
			this.string = string;
			this.icon = icon;
		}
	}

	private String label;
	private Recipe recipe;
	private Date due;
	private State state;
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

	public static State getDefaultState() {
		return State.SCHEDULED;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Nutrition getExtraNutrition() {
		return extraNutrition;
	}

	@Override
	public Amount getNutrition(Nutrient.Type type)
			throws UnitConversionException {
		Amount total = new Amount(0, UnitRegistry.getInstance()
				.getDefaultUnitByType(Unit.TYPE_MASS));
		if (extraNutrition != null) {
			total = extraNutrition.getNutrients().get(type).amount;
		}
		if (recipe != null) {
			total = total.add(recipe.getNutrition(type));
		}
		return total;
	}
}
