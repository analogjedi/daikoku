package com.primateer.daikoku.ui.views.connector;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Event.Listener;
import com.primateer.daikoku.ui.dialogs.DialogView;
import com.primateer.daikoku.ui.views.TabLayout;

public class TabDialogConnector {

	private Dialog dialog;

	public TabDialogConnector(List<DialogView> pages, View launcher,
			String title) {
		this(pages, launcher.getContext(), title);
		launcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TabDialogConnector.this.showDialog();
			}
		});
	}

	public TabDialogConnector(final List<DialogView> pages, Context context,
			String title) {
		dialog = new Dialog(context);
		dialog.setTitle(title);

		TabLayout content = new TabLayout(context);
		for (DialogView page : pages) {
			content.addPage(page.getTitle(), page.getView());
			page.addEventListener(DialogView.DismissedEvent.class,
					new Listener() {
						@Override
						public void onEvent(Event event) {
							dialog.dismiss();
							dialog = null;
						}
					});
		}

		content.setBackgroundColor(Color.WHITE); // FIXME
		dialog.setContentView(content);
	}

	public void showDialog() {
		dialog.show();
	}
}
