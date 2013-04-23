package com.primateer.daikoku.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.PrettyPrinter;
import com.primateer.daikoku.R;
import com.primateer.daikoku.dialogs.FormFragment;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.views.forms.InvalidDataException;
import com.primateer.daikoku.views.forms.ProductForm;
import com.primateer.daikoku.views.forms.VoForm;

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

		final VoForm form = new ProductForm(this);

		Button getStatsButton = new Button(this);
		getStatsButton.setText("print form contents");
		getStatsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Object vo;
				try {
					vo = form.getData();
					if (vo != null) {
						System.out.println(PrettyPrinter.toString((Product) vo));
					}
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Button openDialogButton = new Button(this);
		openDialogButton.setText("open dialog");
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
