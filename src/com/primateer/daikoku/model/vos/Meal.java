package com.primateer.daikoku.model.vos;

import java.util.Date;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;

public class Meal extends Recipe {

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

	private Date due;
	private State state;


	public Meal() {		
	}
	
	public Meal(Recipe recipe) {
		this.setId(recipe.getId());
		this.setLabel(recipe.getLabel());
		this.setFavorite(recipe.isFavorite());
		this.setIngredients(recipe.getIngredients());
		this.setExtraNutrition(recipe.getExtraNutrition());
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

}
