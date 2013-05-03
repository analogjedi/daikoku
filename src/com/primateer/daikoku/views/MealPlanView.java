package com.primateer.daikoku.views;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.actions.Action;
import com.primateer.daikoku.actions.EditFormAction;
import com.primateer.daikoku.db.MealDao;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.Goal;
import com.primateer.daikoku.model.GoalRegistry;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Meal.State;
import com.primateer.daikoku.views.lists.CatalogListAdapter;
import com.primateer.daikoku.views.lists.DataRowListAdapter;
import com.primateer.daikoku.views.widgets.DateWidget;
import com.primateer.daikoku.views.widgets.NutritionWatchWidget;

public class MealPlanView extends LinearLayout {

	private class MealListAdapter extends CatalogListAdapter<Meal> implements
			Observer<Class<ValueObject>> {

		public MealListAdapter() {
			super(Meal.class, new Observer<Meal>() {
				@Override
				public void update(Meal item) {
					Action action = new EditFormAction<Meal>(getContext(), item);
					Application.getInstance().dispatch(action);
				}
			});
			this.addObserver(new Observer<DataRowListAdapter<Meal>>() {
				@Override
				public void update(DataRowListAdapter<Meal> observable) {
					Day day = new Day(datePicker.getData());
					day.setMeals(observable.getData());
					watcher.update(day);
				}
			});
			Data.getInstance().addObserver(this);
		}

		public void reload() {
			this.setData(new MealDao().loadAll(datePicker.getData()));
		}

		@Override
		public void onClick(View v) {
			Data.getInstance().delete(getItemFromView(v));
			super.onClick(v);
		}

		@Override
		public void update(Class<ValueObject> observable) {
			reload();
		}
	}

	private DateWidget datePicker;
	private NutritionWatchWidget watcher;
	private ListView listView;
	private MealListAdapter listAdapter;
	private ImageButton addButton;

	public MealPlanView(Context context) {
		this(context, null);
	}

	public MealPlanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);

		datePicker = new DateWidget(context);
		datePicker.addObserver(new Observer<Date>() {
			@Override
			public void update(Date date) {
				setDate(date);
			}
		});

		watcher = new NutritionWatchWidget(context);
		watcher.setGoals(GoalRegistry.getInstance()
				.getGoals(Goal.Scope.PER_DAY));

		listView = new ListView(context);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listAdapter = new MealListAdapter();
		listView.setAdapter(listAdapter);

		addButton = new ImageButton(context);
		addButton.setImageResource(Application.ICON_ADD);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// FormDialogConnector<Meal> connector = new
				// FormDialogConnector<Meal>(
				// Meal.class, MealPlanView.this.getContext());
				// connector.addObserver(new Observer<Meal>() {
				// @Override
				// public void update(Meal item) {
				// if (Helper.isSameDay(item.getDue(),
				// datePicker.getData())) {
				// listAdapter.add(item);
				// }
				// Data.getInstance().register(item);
				// }
				// });
				// connector.showDialog();
				Meal meal = new Meal();
				meal.setDue(datePicker.getData());
				meal.setFavorite(false);
				meal.setState(State.SCHEDULED);
				Action action = new EditFormAction<Meal>(getContext(), meal);
				Application.getInstance().dispatch(action);
			}
		});

		this.addView(datePicker);
		this.addView(watcher);
		this.addView(listView);
		this.addView(addButton);

		setDate(datePicker.getData());
	}

	public void setDate(Date date) {
		if (!date.equals(datePicker.getData())) {
			datePicker.setData(date);
		}
		listAdapter.reload();
	}

	public void cleanUp() {
		Data.getInstance().removeObserver(listAdapter);
	}
}
