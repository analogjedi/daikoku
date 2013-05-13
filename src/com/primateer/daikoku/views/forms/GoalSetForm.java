package com.primateer.daikoku.views.forms;

import java.util.Collection;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.actions.CatalogAction;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.GoalSet;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Nutrient.Type;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.views.lists.DataRowListAdapter;
import com.primateer.daikoku.views.widgets.AmountWidget;
import com.primateer.daikoku.views.widgets.row.DataRowWidget;

public class GoalSetForm extends LinearLayout implements Form<GoalSet> {

	private static class GoalRowWidget extends LinearLayout implements
			DataRowWidget<Goal> {

		private SimpleObservable<DataRowWidget<Goal>> observable = new SimpleObservable<DataRowWidget<Goal>>();
		private ImageButton deleteButton;
		private TextView nutrientTypeView;
		private TextView goalTypeView;
		private AmountWidget amountView;
		private Goal.Type goalType;
		private Nutrient.Type nutrientType;

		public GoalRowWidget(Context context) {
			this(context, null);
		}

		public GoalRowWidget(Context context, AttributeSet attrs) {
			super(context, attrs);

			deleteButton = new ImageButton(context);
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
					setGoalType(goalType == Goal.Type.MINIMUM ? Goal.Type.MAXIMUM
							: Goal.Type.MINIMUM);
				}
			});
			LinearLayout.LayoutParams goalTypeLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f);
			goalTypeLayout.gravity = Gravity.CENTER;

			amountView = new AmountWidget(context);
			LinearLayout.LayoutParams amountLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.0f);
			amountView.addObserver(new Observer<Amount>() {
				@Override
				public void update(Amount amount) {
					observable.notifyObservers(GoalRowWidget.this);
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
			observable.notifyObservers(this);
		}

		private void setNutrientType(Nutrient.Type type) {
			this.nutrientType = type;
			nutrientTypeView.setText(nutrientType.getName());
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
		public void setOnDeleteRowListener(OnClickListener listener) {
			deleteButton.setOnClickListener(listener);
		}

		@Override
		public void addRowObserver(Observer<DataRowWidget<Goal>> observer) {
			observable.addObserver(observer);
		}

		@Override
		public void removeRowObserver(Observer<DataRowWidget<Goal>> observer) {
			observable.removeObserver(observer);
		}
	}

	private class GoalListAdapter extends DataRowListAdapter<Goal> {
		public GoalListAdapter() {
			super();
		}

		@Override
		protected DataRowWidget<Goal> newWidget(Context context) {
			return new GoalRowWidget(context);
		}
	}

	private ListView listView;
	private GoalListAdapter listAdapter;
	private ImageButton addButton;

	public GoalSetForm(Context context) {
		this(context, null);
	}

	public GoalSetForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);

		listView = new ListView(context);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listAdapter = new GoalListAdapter();
		listView.setAdapter(listAdapter);

		addButton = new ImageButton(context);
		addButton.setImageResource(Application.ICON_ADD);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Catalog<Nutrient.Type> catalog = new Catalog<Nutrient.Type>(
						Nutrient.Type.class);
				catalog.setLoader(new Catalog.Loader<Nutrient.Type>() {
					@Override
					public Collection<Nutrient.Type> load(Catalog<Type> catalog) {
						return NutrientRegistry.getInstance()
								.getAllNutrientTypes();
					}
				});
				catalog.addObserver(new Observer<Nutrient.Type>() {
					@Override
					public void update(Nutrient.Type type) {
						GoalSetForm.this.addGoal(type);
					}
				});
				CatalogAction<Nutrient.Type> action = new CatalogAction<Nutrient.Type>(
						getContext(), catalog, getResources().getString(
								R.string.title_pick_nutrient_type));
				Application.getInstance().dispatch(action);
			}
		});

		this.addView(listView);
		this.addView(addButton);
	}

	private void addGoal(Nutrient.Type type) {
		Goal goal = new Goal(Goal.Type.MINIMUM, Goal.Scope.PER_DAY, type,
				type.getNullAmount());
		listAdapter.add(goal);
		// TODO
	}

	public void cleanUp() {
	}

	@Override
	public View getView() {
		return this;
	}

	@Override
	public void validate() throws InvalidDataException {
		// TODO Auto-generated method stub
	}

	@Override
	public GoalSet getData() throws InvalidDataException {
		return (GoalSet) listAdapter.getData();
	}

	@Override
	public void setData(GoalSet data) throws IllegalArgumentException {
		if (data == null) {
			data = new GoalSet();
		}
		listAdapter.setData(data);
	}

	@Override
	public void clear() {
		listAdapter.clear();
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.title_activity_goals);
	}
}
