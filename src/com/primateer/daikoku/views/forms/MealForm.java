package com.primateer.daikoku.views.forms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.views.connector.CatalogDialogConnector;
import com.primateer.daikoku.views.widgets.AmountWidget;
import com.primateer.daikoku.views.widgets.DateWidget;
import com.primateer.daikoku.views.widgets.LabelWidget;

public class MealForm extends VoForm<Meal> {

	private static class RecipeRow extends LinearLayout {

		private Recipe bufferedData;
		private TextView picker;
		private AmountWidget amount;

		public RecipeRow(Context context) {
			super(context);

			picker = new Button(context);
			LayoutParams pickerLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.5f);
			amount = new AmountWidget(context);
			LayoutParams amountLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.5f);
			this.addView(picker, pickerLayout);
			this.addView(amount, amountLayout);

			CatalogDialogConnector<Recipe> recipeConnector = new CatalogDialogConnector<Recipe>();
			recipeConnector.register(Recipe.class, picker);
			recipeConnector.setSelectionObserver(new Observer<Recipe>() {
				@Override
				public void update(Recipe item) {
					setData(item);
				}
			});
		}

		public void clear() {
			picker.setText(getResources().getString(R.string.placeholder_empty));
			amount.setUnits(UnitRegistry.getInstance().getUnitsByType(
					Unit.Type.COUNT));
			amount.setData(new Amount(0, Unit.UNIT_UNITS));
		}

		public Recipe getData() {
			return bufferedData;
		}

		public void setData(Recipe recipe) {
			this.bufferedData = recipe;
			if (recipe == null) {
				this.clear();
			} else {
				picker.setText(recipe.toString());
				Set<Unit> permissibleUnits = new HashSet<Unit>();
				Amount defaultAmount = recipe.getDefaultAmount();
				permissibleUnits.add(defaultAmount.unit);
				permissibleUnits.addAll(UnitRegistry.getInstance()
						.getUnitsByType(defaultAmount.unit.type));
				permissibleUnits.add(Unit.UNIT_UNITS);
				amount.setUnits(new ArrayList<Unit>(permissibleUnits));
				amount.setData(recipe.getDefaultAmount());
			}
		}
	}

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

	private EditText labelWidget;
	private DateWidget dateWidget;
	private RecipeRow recipeWidget;
	private MealCycleStateWidget stateWidget;

	public MealForm(Context context) {
		super(context);

		labelWidget = new LabelWidget(context);
		dateWidget = new DateWidget(context);
		LayoutParams dateLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.65f);
		recipeWidget = new RecipeRow(context);
		stateWidget = new MealCycleStateWidget(context);
		LayoutParams stateLayout = new LayoutParams(0,
				LayoutParams.MATCH_PARENT, 0.35f);
		stateLayout.gravity = Gravity.CENTER_VERTICAL;

		LinearLayout dateLine = new LinearLayout(context);
		dateLine.addView(stateWidget, stateLayout);
		dateLine.addView(dateWidget, dateLayout);

		this.addView(labelWidget);
		this.addView(dateLine);
		this.addView(recipeWidget);

		recipeWidget.setData(null);
	}

	@Override
	public void validate() throws InvalidDataException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.meal);
	}

	@Override
	protected Meal gatherData() throws InvalidDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void fillFields(Meal data) throws IllegalArgumentException {
		dateWidget.setData(data.getDue());
		labelWidget.setText(data.getLabel());
		recipeWidget.setData(data.getRecipe());
		// TODO
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	public void setDate(Date date) {
		dateWidget.setData(date);
	}

}
