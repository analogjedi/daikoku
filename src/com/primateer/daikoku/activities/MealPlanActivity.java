package com.primateer.daikoku.activities;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.primateer.daikoku.R;
import com.primateer.daikoku.dialogs.DatePickerFragment;

public class MealPlanActivity extends FragmentActivity implements
		DatePickerDialog.OnDateSetListener {

	private Date currentDate = new Date();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meal_plan);

		updateDate();
	}

	private void updateDate() {
		View dateLabel = this.findViewById(R.id.lbl_date);
		((Button) dateLabel).setText(DateFormat
				.getDateInstance(DateFormat.LONG).format(currentDate));
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.meal_plan, menu);
	// return true;
	// }

	private void addDays(int d) {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, d);
		currentDate = c.getTime();
		updateDate();
	}

	public void incrDate(View v) {
		addDays(1);
	}

	public void decrDate(View v) {
		addDays(-1);
	}

	public void pickDate(View v) {
		DialogFragment dp = new DatePickerFragment().setDate(currentDate);
		dp.show(getSupportFragmentManager(), "datePicker");
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		currentDate = c.getTime();
		updateDate();
	}
}
