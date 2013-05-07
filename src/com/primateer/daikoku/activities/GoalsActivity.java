package com.primateer.daikoku.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.primateer.daikoku.views.GoalsView;

public class GoalsActivity extends FragmentActivity {

	GoalsView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new GoalsView(this);
		this.setContentView(view);
	}
	
	@Override
	protected void onDestroy() {		
		view.cleanUp();
		super.onDestroy();
	}
}
