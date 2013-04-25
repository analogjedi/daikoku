package com.primateer.daikoku.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.dialogs.FormFragment;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.views.Catalog;
import com.primateer.daikoku.views.ListCatalog;
import com.primateer.daikoku.views.forms.InvalidDataException;
import com.primateer.daikoku.views.forms.ProductForm;
import com.primateer.daikoku.views.widgets.Separator;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout views = new LinearLayout(this);
		views.setOrientation(LinearLayout.VERTICAL);
		this.setContentView(views);

		Nutrition lentilsNutrition = new Nutrition();
		lentilsNutrition.setNutrient(new Nutrient(Nutrient.TYPE_ENERGY,
				new Amount("336kcal")));
		lentilsNutrition.setNutrient(new Nutrient(Nutrient.TYPE_PROTEIN,
				new Amount("23g")));
		lentilsNutrition.setNutrient(new Nutrient(Nutrient.TYPE_CARBS,
				new Amount("50g")));
		lentilsNutrition.setNutrient(new Nutrient(Nutrient.TYPE_FAT,
				new Amount("1.6g")));

		final Product lentils = new Product();
		lentils.setLabel("lentils");
		lentils.setNutrition(lentilsNutrition);
		lentils.setAmount(new Amount("500g"));
		lentils.setUnits(0);

		final Product packagedLentils = new Product();
		packagedLentils.setLabel("packaged lentils");
		packagedLentils.setNutrition(lentilsNutrition);
		packagedLentils.setAmount(new Amount("1kg"));
		packagedLentils.setUnits(4);

		Button getStatsButton = new Button(this);
		getStatsButton.setText("open product catalog");
		getStatsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Catalog<Product> catalog = new ListCatalog<Product>(
						MainActivity.this);
				catalog.setClass(Product.class);
				catalog.add(lentils);
				catalog.add(packagedLentils);
				catalog.add(lentils);
				catalog.add(packagedLentils);
				catalog.add(lentils);
				catalog.add(packagedLentils);
				catalog.add(lentils);
				catalog.add(packagedLentils);
				catalog.add(lentils);
				catalog.add(packagedLentils);

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
						"form_dialog");
			}
		});

		views.addView(getStatsButton);
		views.addView(openDialogButton);
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
