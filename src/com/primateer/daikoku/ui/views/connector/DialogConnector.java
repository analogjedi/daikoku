package com.primateer.daikoku.ui.views.connector;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.ui.dialogs.DialogView;

public class DialogConnector {

	private Dialog dialog;

	public DialogConnector(DialogView view, View launcher, String title) {
		this(view, launcher.getContext(), title);
		launcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogConnector.this.showDialog();
			}
		});
	}

	public DialogConnector(DialogView view, Context context, String title) {
		view.addEventListener(DialogView.DismissedEvent.class,
				new Event.Listener() {
					@Override
					public void onEvent(Event event) {
						dialog.dismiss();
					}
				});
		dialog = new Dialog(context);
		dialog.setTitle(title);
		ViewGroup content = (ViewGroup) view;
		content.setBackgroundColor(Color.WHITE); // FIXME
		dialog.setContentView(content);
	}

	public void showDialog() {
		dialog.show();
	}
}
