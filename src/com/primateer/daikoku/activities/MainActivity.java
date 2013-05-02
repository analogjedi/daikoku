package com.primateer.daikoku.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.primateer.daikoku.R;
import com.primateer.daikoku.dialogs.FormFragment;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.views.Catalog;
import com.primateer.daikoku.views.forms.MealForm;
import com.primateer.daikoku.views.forms.ProductForm;
import com.primateer.daikoku.views.forms.RecipeForm;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout views = new LinearLayout(this);
		views.setOrientation(LinearLayout.VERTICAL);
		this.setContentView(views);

		Button getStatsButton = new Button(this);
		getStatsButton.setText("open product catalog");
		getStatsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Catalog<Product> catalog = new Catalog<Product>(
						MainActivity.this, Product.class, null);
				Dialog dialog = new Dialog(MainActivity.this);
				dialog.setTitle("PRODUCT CATALOG");
				ViewGroup content = (ViewGroup) catalog;
				content.setBackgroundColor(Color.WHITE); // FIXME
				dialog.setContentView(content);
				dialog.show();
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
						"product");
			}
		});

		Button openRecipeButton = new Button(this);
		openRecipeButton.setText("open recipe form");
		openRecipeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FormFragment frag = new FormFragment();
				frag.setForm(new RecipeForm(MainActivity.this));
				frag.show(MainActivity.this.getSupportFragmentManager(),
						"recipe");
			}
		});

		Button mealPlanButton = new Button(this);
		mealPlanButton.setText("start meal plan activity");
		mealPlanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						MealPlanActivity.class));
			}
		});

		Button openMealButton = new Button(this);
		openMealButton.setText("open meal form");
		openMealButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FormFragment frag = new FormFragment();
				frag.setForm(new MealForm(MainActivity.this));
				frag.show(MainActivity.this.getSupportFragmentManager(),
						"recipe");
			}
		});

		views.addView(getStatsButton);
		views.addView(openDialogButton);
		views.addView(openRecipeButton);
		views.addView(mealPlanButton);
		views.addView(openMealButton);
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
