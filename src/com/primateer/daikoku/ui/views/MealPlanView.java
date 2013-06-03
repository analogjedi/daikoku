package com.primateer.daikoku.ui.views;

import java.util.Collection;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.GoalRegistry;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Meal.State;
import com.primateer.daikoku.ui.actions.Action;
import com.primateer.daikoku.ui.actions.DeleteDataAction;
import com.primateer.daikoku.ui.actions.FormAction;
import com.primateer.daikoku.ui.views.lists.CatalogListAdapter;
import com.primateer.daikoku.ui.views.lists.DataRowListAdapter;
import com.primateer.daikoku.ui.views.widgets.AddButton;
import com.primateer.daikoku.ui.views.widgets.DateWidget;
import com.primateer.daikoku.ui.views.widgets.NutritionWatchWidget;

public class MealPlanView extends LinearLayout {

	private class SwipeDetector extends SimpleOnGestureListener {
		public static final int MIN_VELOCITY = 600;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Math.abs(velocityX) >= MIN_VELOCITY
					&& Math.abs(velocityX) > Math.abs(velocityY) * 1.5) {
				if (velocityX > 0) {
					datePicker.addDays(-1);
				} else {
					datePicker.addDays(1);
				}
			}
			return true;
		}
	}

	private class MealListAdapter extends CatalogListAdapter<Meal> implements
			Event.Listener {

		public MealListAdapter(Catalog<Meal> catalog) {
			super(catalog);

			this.addEventListener(DataRowListAdapter.ListChangedEvent.class,
					new Event.Listener() {
						@Override
						public void onEvent(Event event) {
							Day day = new Day(datePicker.getData());
							day.addAll(listAdapter.getData());
							watcher.update(day);
						}
					});
			DBController.getInstance().addEventListener(
					DBController.DBChangedEvent.class, this);
		}

		@Override
		public void onDelete(View v) {
			Action action = new DeleteDataAction<Meal>(getItemFromView(v),
					getContext());
			Application.getInstance().dispatch(action);
		}

		@Override
		public void onEvent(Event event) {
			if (event instanceof DBController.DBChangedEvent) {
				super.reload();
			}
		}
	}

	private DateWidget datePicker;
	private NutritionWatchWidget watcher;
	private ListView listView;
	private MealListAdapter listAdapter;
	private AddButton addButton;

	public MealPlanView(Context context) {
		this(context, null);
	}

	public MealPlanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);

		datePicker = new DateWidget(context);
		datePicker.addEventListener(DateWidget.DateChangedEvent.class,
				new Event.Listener() {
					@Override
					public void onEvent(Event event) {
						setDate(((DateWidget.DateChangedEvent) event).date);
					}
				});

		watcher = new NutritionWatchWidget(context);
		watcher.setGoals(GoalRegistry.getInstance()
				.getGoals(Goal.Scope.PER_DAY));

		listView = new ListView(context);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		Catalog<Meal> catalog = new Catalog<Meal>(Meal.class);
		catalog.setLoader(new Catalog.Loader<Meal>() {
			@Override
			public Collection<Meal> load(Catalog<Meal> catalog) {
				return DBController.getInstance().loadAllMeals(
						datePicker.getData());
			}
		});
		listAdapter = new MealListAdapter(catalog);
		listView.setAdapter(listAdapter);
		// fling detection on list
		final GestureDetector detector = new GestureDetector(context,
				new SwipeDetector());
		listView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return !detector.onTouchEvent(event);
			}
		});

		addButton = new AddButton(context);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Meal meal = new Meal();
				meal.setDue(datePicker.getData());
				meal.setFavorite(false);
				meal.setState(State.SCHEDULED);
				Action action = new FormAction<Meal>(getContext(), meal);
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
		DBController.getInstance().removeEventListener(
				DBController.DBChangedEvent.class, listAdapter);
	}
}
