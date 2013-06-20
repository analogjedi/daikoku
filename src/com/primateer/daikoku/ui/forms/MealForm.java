package com.primateer.daikoku.ui.forms;

import java.util.Date;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.ui.widgets.DateWidget;

public class MealForm extends RecipeForm {

	private static class MealCycleStateWidget extends LinearLayout {

		private Meal.State state = Meal.getDefaultState();
		private ImageView icon;
		private TextView text;

		public MealCycleStateWidget(Context context) {
			super(context);

			icon = new ImageView(context);
			text = new TextView(context);
			text.setGravity(Gravity.CENTER_VERTICAL);
			this.addView(icon, new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.MATCH_PARENT));
			this.addView(text, new LayoutParams(0, LayoutParams.MATCH_PARENT,
					1.0f));

			this.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Meal.State[] states = Meal.State.values();
					MealCycleStateWidget.this.setData(states[(state.ordinal() + 1)
							% states.length]);
				}
			});
			this.setData(Meal.getDefaultState());
		}

		public Meal.State getData() {
			return state;
		}

		public void setData(Meal.State state) {
			this.state = state;
			icon.setImageResource(state.icon);
			text.setText(state.string);
		}
	}

	@SuppressWarnings("unused")
	private static class MealRadioStateWidget extends RadioGroup {

		private Meal.State state = Meal.getDefaultState();

		public MealRadioStateWidget(Context context) {
			super(context);

			for (Meal.State state : Meal.State.values()) {
				RadioButton option = new RadioButton(context);
				option.setId(state.ordinal());
				option.setText(getResources().getString(state.string));
				option.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setData(Meal.State.values()[((RadioButton) v).getId()]);
					}
				});
				this.addView(option);
			}

			this.setData(Meal.getDefaultState());
		}

		public Meal.State getData() {
			return state;
		}

		public void setData(Meal.State state) {
			this.state = state;
			this.check(state.ordinal());
		}
	}

	private DateWidget dateWidget;
	private MealCycleStateWidget stateWidget;

	public MealForm(Context context) {
		super(context);

		dateWidget = new DateWidget(context);
		LayoutParams dateLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.65f);

		stateWidget = new MealCycleStateWidget(context);
		LayoutParams stateLayout = new LayoutParams(0,
				LayoutParams.MATCH_PARENT, 0.35f);
		stateLayout.gravity = Gravity.CENTER_VERTICAL;

		LinearLayout dateLine = new LinearLayout(context);
		dateLine.addView(stateWidget, stateLayout);
		dateLine.addView(dateWidget, dateLayout);
		
		this.setFavable(true);
		this.setFavorite(false);

		this.addView(dateLine, 1);
	}

	@Override
	public void validate() throws InvalidDataException {
		super.validate();
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.meal);
	}

	@Override
	protected Meal gatherData() throws InvalidDataException {
		Meal data = new Meal(super.gatherData());
		data.setDue(dateWidget.getData());
		data.setState(stateWidget.getData());
		return data;
	}

	@Override
	protected void fillFields(Recipe recipe) throws IllegalArgumentException {
		super.fillFields(recipe);
		if (recipe instanceof Meal) {
			Meal meal = (Meal) recipe;
			dateWidget.setData(meal.getDue());
			stateWidget.setData(meal.getState());
		}
	}

	public void setDate(Date date) {
		dateWidget.setData(date);
	}

}
