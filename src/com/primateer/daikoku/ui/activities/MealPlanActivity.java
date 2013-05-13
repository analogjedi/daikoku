package com.primateer.daikoku.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.primateer.daikoku.ui.views.MealPlanView;

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
