package com.primateer.daikoku.ui.forms;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Event;
import com.primateer.daikoku.R;
import com.primateer.daikoku.Event.Listener;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.GoalSet;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Nutrient.Type;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Scope;
import com.primateer.daikoku.ui.dialogs.CatalogView;
import com.primateer.daikoku.ui.dialogs.DialogViewConnector;
import com.primateer.daikoku.ui.lists.DataRowListAdapter;
import com.primateer.daikoku.ui.lists.DataRowWidget;
import com.primateer.daikoku.ui.widgets.AddButton;
import com.primateer.daikoku.ui.widgets.DeleteRowButton;
import com.primateer.daikoku.ui.widgets.ListWidget;

public class GoalSetForm extends Form<GoalSet> {

	private class GoalRowWidget extends LinearLayout implements
			DataRowWidget<Goal> {

		private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();
		private DeleteRowButton deleteButton;
		private TextView nutrientTypeView;
		private TextView goalTypeView;
		private AmountForm amountView;
		private Goal.Type goalType;
		private Nutrient.Type nutrientType;

		public GoalRowWidget(Context context) {
			this(context, null);
		}

		public GoalRowWidget(Context context, AttributeSet attrs) {
			super(context, attrs);

			deleteButton = new DeleteRowButton(context, dispatcher);
			deleteButton.setImageResource(Application.ICON_REMOVE);
			deleteButton.setBackgroundColor(Color.TRANSPARENT);
			LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f);
			deleteLayout.gravity = Gravity.CENTER;

			nutrientTypeView = new TextView(context);
			LinearLayout.LayoutParams nutrientLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.6f);
			nutrientLayout.gravity = Gravity.CENTER;

			goalTypeView = new TextView(context);
			goalTypeView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listAdapter.swapMinMax(nutrientType);
				}
			});
			LinearLayout.LayoutParams goalTypeLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f);
			goalTypeLayout.gravity = Gravity.CENTER;

			amountView = new AmountForm(context);
			LinearLayout.LayoutParams amountLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f);
			amountView.addEventListener(AmountForm.AmountChangedEvent.class,
					new Event.Listener() {
						@Override
						public void onEvent(Event event) {
							dispatcher.dispatch(new ChangedEvent());
						}
					});
			amountLayout.gravity = Gravity.CENTER;

			this.addView(deleteButton, deleteLayout);
			this.addView(nutrientTypeView, nutrientLayout);
			this.addView(goalTypeView, goalTypeLayout);
			this.addView(amountView, amountLayout);
		}

		@Override
		public View getView() {
			return this;
		}

		@Override
		public void storeRowPosition(int pos) {
			this.setTag(pos);

		}

		@Override
		public int restoreRowPosition() {
			return (Integer) this.getTag();
		}

		private void setGoalType(Goal.Type type) {
			this.goalType = type;
			switch (goalType) {
			case MINIMUM:
				goalTypeView.setText(R.string.abbrev_minimum);
				goalTypeView.setTextColor(Goal.Status.ACHIEVABLE.color);
				break;
			case MAXIMUM:
				goalTypeView.setText(R.string.abbrev_maximum);
				goalTypeView.setTextColor(Goal.Status.FAILED.color);
			}
			dispatcher.dispatch(new ChangedEvent());
		}

		private void setNutrientType(Nutrient.Type type) {
			this.nutrientType = type;
			nutrientTypeView.setText(nutrientType.getName() + " ("
					+ nutrientType.id + ")");
		}

		@Override
		public void setRowData(Goal data) {
			setNutrientType(data.nutrientType);
			setGoalType(data.type);
			amountView.setUnits(data.amount.unit.type);
			amountView.setData(data.amount);
		}

		@Override
		public Goal getRowData() throws InvalidDataException {
			return new Goal(goalType, Goal.Scope.PER_DAY, nutrientType,
					amountView.getData());
		}

		@Override
		public void addEventListener(Class<? extends Event> type,
				Listener listener) {
			dispatcher.addEventListener(type, listener);
		}

		@Override
		public void removeEventListener(Class<? extends Event> type,
				Listener listener) {
			dispatcher.removeEventListener(type, listener);
		}
	}

	private class GoalListAdapter extends DataRowListAdapter<Goal> {
		public GoalListAdapter() {
			super();
		}

		public void swapMinMax(Type nutrientType) {
			getGoals().swapMinMax(nutrientType);
			notifyObservers();
		}

		@Override
		protected DataRowWidget<Goal> newWidget(Context context) {
			return new GoalRowWidget(context);
		}

		public GoalSet getGoals() {
			return (GoalSet) data;
		}

		public void add(Type type) {
			Goal goal = getGoals().getFreeGoal(type, Scope.PER_DAY);
			super.add(goal);
		}
	}

	private ListWidget listView;
	private GoalListAdapter listAdapter;
	private AddButton addButton;

	public GoalSetForm(Context context) {
		this(context, null);
	}

	public GoalSetForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);

		listView = new ListWidget(context);
		listAdapter = new GoalListAdapter();
		listView.setAdapter(listAdapter);

		addButton = new AddButton(context);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Catalog<Nutrient.Type> catalog = new Catalog<Nutrient.Type>(
						Nutrient.Type.class);
				catalog.setEditable(false);
				catalog.setLoader(new Catalog.Loader<Nutrient.Type>() {
					@Override
					public Collection<Nutrient.Type> load(Catalog<Type> catalog) {
						Collection<Nutrient.Type> types = new ArrayList<Nutrient.Type>();
						types.addAll(NutrientRegistry.getInstance()
								.getAllNutrientTypes());
						types.removeAll(listAdapter.getGoals()
								.getOccupiedTypes());
						return types;
					}
				});
				catalog.addEventListener(Catalog.SelectionEvent.class,
						new Event.Listener() {
							@SuppressWarnings("unchecked")
							@Override
							public void onEvent(Event event) {
								Catalog.SelectionEvent<Nutrient.Type> ev = (Catalog.SelectionEvent<Nutrient.Type>) event;
								listAdapter.add(ev.selection);
							}
						});
				catalog.setTitle(getResources().getString(
						R.string.title_pick_nutrient_type));

				new DialogViewConnector(new CatalogView<Nutrient.Type>(
						getContext(), catalog), getContext()).showDialog();
			}
		});

		this.addView(listView);
		this.addView(addButton);
	}

	@Override
	public void validate() throws InvalidDataException {
		// TODO Auto-generated method stub
	}

	@Override
	public void clear() {
		listAdapter.clear();
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.title_activity_goals);
	}

	@Override
	protected GoalSet gatherData() throws InvalidDataException {
		return (GoalSet) listAdapter.getData();
	}

	@Override
	protected void fillFields(GoalSet data) throws IllegalArgumentException {
		if (data == null) {
			data = new GoalSet();
		}
		listAdapter.setData(data);
	}
}
