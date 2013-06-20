package com.primateer.daikoku.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.primateer.daikoku.Event;

public class DialogViewConnector {

	private Dialog dialog;

	public DialogViewConnector(DialogView view, Context context) {
		view.addEventListener(DialogView.DismissedEvent.class,
				new Event.Listener() {
					@Override
					public void onEvent(Event event) {
						dialog.dismiss();
						dialog = null;
					}
				});
		dialog = new Dialog(context);
		dialog.setTitle(view.getTitle());
		ViewGroup content = (ViewGroup) view;
		content.setBackgroundColor(Color.WHITE); // FIXME
		dialog.setContentView(content);
	}

	public void connectLauncher(View launcher) {
		launcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogViewConnector.this.showDialog();
			}
		});
	}

	public void showDialog() {
		dialog.show();
	}
}
