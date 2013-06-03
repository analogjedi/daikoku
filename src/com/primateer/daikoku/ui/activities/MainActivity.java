package com.primateer.daikoku.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;

public class MainActivity extends FragmentActivity {

	ViewGroup views;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		views = new LinearLayout(this);
		((LinearLayout) views).setOrientation(LinearLayout.VERTICAL);
		this.setContentView(views);

		this.addActivity(MealPlanActivity.class, Application.ICON_CALENDAR,
				R.string.title_activity_meal_plan);
		this.addActivity(ShoppingActivity.class, Application.ICON_LIST,
				R.string.title_activity_shopping);
		this.addActivity(GoalsActivity.class, Application.ICON_GOALS,
				R.string.title_activity_goals);
//		this.addActivity(null, Application.ICON_SETTINGS,
//				R.string.title_activity_settings);
	}

	private void addActivity(final Class<? extends Activity> activity,
			int icon, int label) {
		Button button = new Button(this);
		button.setText(getResources().getText(label));
		button.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
				.getDrawable(icon), null, null);
		if (activity != null) {
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainActivity.this, activity));
				}
			});
		} else {
			button.setEnabled(false);
		}
		button.setFocusable(false);
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 1.0f);
		views.addView(button, layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void navMealPlan(View v) {
		this.startActivity(new Intent(this, MealPlanActivity.class));
	}
}
