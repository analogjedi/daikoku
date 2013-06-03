package com.primateer.daikoku.ui.views.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.ui.views.widgets.row.DataRowWidget;

@SuppressLint("ViewConstructor")
public class DeleteRowButton extends ImageButton {

	public DeleteRowButton(Context context, Event.Dispatcher dispatcher) {
		this(context, null, dispatcher);
	}

	public DeleteRowButton(Context context, AttributeSet attrs,
			final Event.Dispatcher dispatcher) {
		super(context, attrs);

		this.setImageResource(Application.ICON_DELETE);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setFocusable(false);
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dispatcher.dispatch(new DataRowWidget.DeleteRequestEvent());
			}
		});
	}

}
