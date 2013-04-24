package com.primateer.daikoku.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.primateer.daikoku.R;
import com.primateer.daikoku.dialogs.FormFragment;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.views.ListCatalog;
import com.primateer.daikoku.views.forms.ProductForm;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		//
		// ViewGroup views = (ViewGroup) this
		// .findViewById(R.id.activity_main_layout);
		LinearLayout views = new LinearLayout(this);
		views.setOrientation(LinearLayout.VERTICAL);
		ScrollView scroll = new ScrollView(this);
		// scroll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT));
		// scroll.addView(views);
		this.setContentView(views);

		Button getStatsButton = new Button(this);
		getStatsButton.setText("open product catalog");
		getStatsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ListCatalog<Product> catalog = new ListCatalog<Product>(MainActivity.this);
				MainActivity.this.setContentView(catalog);
//				MainActivity.this.setContentView(new Button(MainActivity.this));
			}
		});

		Button openDialogButton = new Button(this);
		openDialogButton.setText("open product form");
		openDialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FormFragment frag = new FormFragment();
				frag.setForm(new ProductForm(MainActivity.this));
				frag.show(MainActivity.this.getSupportFragmentManager(),
						"form_dialog");
			}
		});

		views.addView(getStatsButton);
		views.addView(openDialogButton);
		// views.addView(form);
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
