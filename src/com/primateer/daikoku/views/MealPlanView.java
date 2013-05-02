package com.primateer.daikoku.views;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.db.MealDao;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.Day;
import com.primateer.daikoku.model.Goal;
import com.primateer.daikoku.model.GoalRegistry;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.views.connector.FormDialogConnector;
import com.primateer.daikoku.views.lists.CatalogListAdapter;
import com.primateer.daikoku.views.lists.DataRowListAdapter;
import com.primateer.daikoku.views.lists.MealListAdapter;
import com.primateer.daikoku.views.widgets.DateWidget;
import com.primateer.daikoku.views.widgets.NutritionWatchWidget;

public class MealPlanView extends LinearLayout {

	private DateWidget datePicker;
	private NutritionWatchWidget watcher;
	private ListView listView;
//	private MealListAdapter listAdapter;
	private CatalogListAdapter<Meal> listAdapter;
	private ImageButton addButton;

	public MealPlanView(Context context) {
		this(context, null);
	}

	public MealPlanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);

		datePicker = new DateWidget(context);

		watcher = new NutritionWatchWidget(context);
		watcher.setGoals(GoalRegistry.getInstance()
				.getGoals(Goal.Scope.PER_DAY));
		watcher.setWatchList(NutrientRegistry.getInstance().getWatchList());
		watcher.update(null);

		listView = new ListView(context);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//		listAdapter = new MealListAdapter();
		listAdapter = new CatalogListAdapter<Meal>();
		listAdapter.register(Meal.class, null);
		listAdapter.addObserver(new Observer<DataRowListAdapter<Meal>>() {
			@Override
			public void update(DataRowListAdapter<Meal> observable) {
				Day day = new Day(datePicker.getData());
				day.setMeals(observable.getData());
				watcher.update(day);
			}
		});
		listView.setAdapter(listAdapter);

		addButton = new ImageButton(context);
		addButton.setImageResource(Application.ICON_ADD);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FormDialogConnector<Meal> connector = new FormDialogConnector<Meal>();
				connector.register(Meal.class, MealPlanView.this.getContext());
				connector.addObserver(new Observer<Meal>() {
					@Override
					public void update(Meal item) {
						listAdapter.add(item);
						Data.getInstance().register(item);
					}
				});
				connector.showDialog();
			}
		});

		this.addView(datePicker);
		this.addView(watcher);
		this.addView(listView);
		this.addView(addButton);
	}

	public void setDate(Date date) {
		datePicker.setData(date);
		listAdapter.setData(new MealDao().loadAll(date));
	}
}
