package com.primateer.daikoku.ui.views.widgets;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Event.Listener;
import com.primateer.daikoku.model.Event.SimpleDispatcher;
import com.primateer.daikoku.ui.dialogs.DatePickerFragment;

public class DateWidget extends LinearLayout implements
		DatePickerDialog.OnDateSetListener, Event.Dispatcher {
	
	public static class DateChangedEvent extends Event {
		public final Date date;
		public DateChangedEvent(Date date) {
			this.date = date;
		}
	}

	private Event.SimpleDispatcher dispatcher = new SimpleDispatcher();
	private TextView dateView;
	private Button incrButton;
	private Button decrButton;
	private Date currentDate = new Date();

	public DateWidget(Context context) {
		this(context, null);
	}

	public DateWidget(final Context context, AttributeSet attrs) {
		super(context, attrs);

		dateView = new TextView(context);
		dateView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment dp = new DatePickerFragment().setCallback(
						DateWidget.this).setDate(currentDate);
				dp.show(((FragmentActivity) context)
						.getSupportFragmentManager(), null);
			}
		});
		dateView.setGravity(Gravity.CENTER);
		LayoutParams dateLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1.0f);

		incrButton = new Button(context);
		incrButton.setText(">");
		incrButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addDays(1);
			}
		});
		LayoutParams incrLayout = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		decrButton = new Button(context);
		decrButton.setText("<");
		decrButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addDays(-1);
			}
		});
		LayoutParams decrLayout = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		updateDate();

		this.addView(decrButton, decrLayout);
		this.addView(dateView, dateLayout);
		this.addView(incrButton, incrLayout);
	}

	private void updateDate() {
		dateView.setText(DateFormat.getDateInstance(DateFormat.LONG).format(
				currentDate));		
		dispatcher.dispatch(new DateChangedEvent(currentDate));
	}
	
	public Date getData() {
		return currentDate;
	}
	
	public void setData(Date data) {
		this.currentDate = data;
		updateDate();
	}

	public void addDays(int d) {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DATE, d);
		currentDate = c.getTime();
		updateDate();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		currentDate = c.getTime();
		updateDate();
	}

	@Override
	public void addEventListener(Class<? extends Event> type, Listener listener) {
		dispatcher.addEventListener(type, listener);
	}

	@Override
	public void removeEventListener(Class<? extends Event> type,
			Listener listener) {
		dispatcher.removeEventListener(type, listener);
	}
}
