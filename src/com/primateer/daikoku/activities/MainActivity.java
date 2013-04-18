package com.primateer.daikoku.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.views.NutritionForm;
import com.primateer.daikoku.widgets.NutrientRowWidget;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ViewGroup views = (ViewGroup) this
				.findViewById(R.id.activity_main_layout);

		NutrientRowWidget nut1 = new NutrientRowWidget(this);
		nut1.setNutrientType(Nutrient.TYPE_ENERGY);
		nut1.setAmount(new Amount("333.3kcal"));

		NutrientRowWidget nut2 = new NutrientRowWidget(this);
		nut2.setNutrientType(Nutrient.TYPE_PROTEIN);
		nut2.setAmount(new Amount("20.7g"));

		final NutrientRowWidget nut3 = new NutrientRowWidget(this);
		nut3.setNutrientType(Nutrient.TYPE_CARBS);
		nut3.setOnDeleteListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this,
						(CharSequence) nut3.getAmount().toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
		
		NutritionForm nutrition = new NutritionForm(this);

		views.addView(nut1);
		views.addView(nut2);
		views.addView(nut3);
		views.addView(nutrition);
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
