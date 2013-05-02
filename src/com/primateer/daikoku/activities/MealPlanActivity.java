package com.primateer.daikoku.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.primateer.daikoku.views.MealPlanView;

public class MealPlanActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_meal_plan);
		this.setContentView(new MealPlanView(this));
	}
}
