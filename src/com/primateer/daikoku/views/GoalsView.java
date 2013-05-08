package com.primateer.daikoku.views;

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
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.views.forms.InvalidDataException;
import com.primateer.daikoku.views.lists.DataRowListAdapter;
import com.primateer.daikoku.views.widgets.AmountWidget;
import com.primateer.daikoku.views.widgets.row.DataRowWidget;

public class GoalsView extends LinearLayout {

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
			deleteButton.setImageResource(Application.ICON_DELETE);
			deleteButton.setBackgroundColor(Color.TRANSPARENT);
			LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.25f);
			deleteLayout.gravity = Gravity.CENTER;

			nutrientTypeView = new TextView(context);

			goalTypeView = new TextView(context);

			amountView = new AmountWidget(context);

			this.addView(deleteButton);
			this.addView(nutrientTypeView);
			this.addView(goalTypeView);
			this.addView(amountView);
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
				break;
			case MAXIMUM:
				goalTypeView.setText(R.string.abbrev_maximum);
			}
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
			// TODO validation
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
		@Override
		protected DataRowWidget<Goal> newWidget(Context context) {
			return new GoalRowWidget(context);
		}
	}

	private ListView listView;
	private GoalListAdapter listAdapter;
	private ImageButton addButton;

	public GoalsView(Context context) {
		this(context, null);
	}

	public GoalsView(Context context, AttributeSet attrs) {
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
//		addButton.setOnClickListener(new OnClickListener() {			
//			@Override
//			public void onClick(View v) {
//				Catalog<Nutrient.Type> catalog = new Catalog<Nutrient.Type>(Nutrient.Type.class);
//				catalog.setOnSelectionListener(new Catalog.OnSelectionListener<Nutrient.Type>() {
//					@Override
//					public void onSelection(Nutrient.Type type) {
//						GoalsView.this.addGoal(type);
//					}
//				});
//				CatalogAction action = new CatalogAction(catalog);
//				Application.getInstance().dispatch(action);
//			}
//		});

		this.addView(listView);
		this.addView(addButton);
	}
	
	private void addGoal(Nutrient.Type type) {
		// TODO
	}

	public void cleanUp() {
	}
}
