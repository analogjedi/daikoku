package com.primateer.daikoku;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

	private Date date = new Date();

	public DatePickerFragment setDate(Date date) {
		this.date = date;
		return this;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(getActivity(),
				(DatePickerDialog.OnDateSetListener) getActivity(), year,
				month, day);
	}
}
