package com.primateer.daikoku;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MealPlanActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meal_plan);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.meal_plan, menu);
		return true;
	}

}
