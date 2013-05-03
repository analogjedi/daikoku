package com.primateer.daikoku.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.primateer.daikoku.views.MealPlanView;

public class MealPlanActivity extends FragmentActivity {

	MealPlanView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new MealPlanView(this);
		this.setContentView(view);
	}
	
	@Override
	protected void onDestroy() {		
		view.cleanUp();
		super.onDestroy();
	}
}
