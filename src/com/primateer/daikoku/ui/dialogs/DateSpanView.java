package com.primateer.daikoku.ui.dialogs;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Event;
import com.primateer.daikoku.R;
import com.primateer.daikoku.Event.Listener;
import com.primateer.daikoku.ui.widgets.DateWidget;

public class DateSpanView extends LinearLayout implements DialogView {

	public static class DatesPickedEvent extends Event {
		public final Date start;
		public final Date end;

		public DatesPickedEvent(Date start, Date end) {
			this.start = start;
			this.end = end;
		}
	}

	private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();

	public DateSpanView(Context context) {
		this(context, null);
	}

	public DateSpanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);

		final DateWidget startPicker = new DateWidget(context);

		final DateWidget endPicker = new DateWidget(context);

		ImageButton okButton = new ImageButton(context);
		okButton.setImageResource(Application.ICON_ACCEPT);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatcher.dispatch(new DatesPickedEvent(startPicker.getData(),
						endPicker.getData()));
				dispatcher.dispatch(new DialogView.DismissedEvent());
			}
		});

		this.addView(startPicker, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		this.addView(endPicker, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));		
		this.addView(okButton, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
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

	@Override
	public String getTitle() {
		return getResources().getString(R.string.title_date_period);
	}

	@Override
	public View getView() {
		return this;
	}

}
