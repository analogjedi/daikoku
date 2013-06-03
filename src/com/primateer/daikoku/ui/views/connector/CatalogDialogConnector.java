package com.primateer.daikoku.ui.views.connector;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.views.CatalogView;

public class CatalogDialogConnector<T extends ValueObject> {

	private CatalogView<T> catalogView;
	private Dialog dialog;

	public CatalogDialogConnector(Catalog<T> catalog, View launcher,
			String title) {
		this(catalog, launcher.getContext(), title);
		launcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CatalogDialogConnector.this.showDialog();
			}
		});
	}

	public CatalogDialogConnector(final Catalog<T> catalog, Context context,
			String title) {
		final Event.Listener dbListener = new Event.Listener() {
			@Override
			public void onEvent(Event event) {
				DBController.DBChangedEvent e = (DBController.DBChangedEvent) event;
				if (e.type.equals(catalog.dataClass)) {
					catalogView.reload();
				}
			}
		};
		DBController.getInstance().addEventListener(
				DBController.DBChangedEvent.class, dbListener);
		catalogView = new CatalogView<T>(context, catalog);
		dialog = new Dialog(context);
		dialog.setTitle(title);
		ViewGroup content = (ViewGroup) catalogView;
		content.setBackgroundColor(Color.WHITE); // FIXME
		dialog.setContentView(content);
		catalog.addEventListener(Catalog.SelectionEvent.class,
				new Event.Listener() {
					@Override
					public void onEvent(Event event) {
						dialog.dismiss();
						DBController.getInstance().removeEventListener(
								DBController.DBChangedEvent.class, dbListener);
						dialog = null;
					}
				});
	}

	public void showDialog() {
		dialog.show();
	}

	public CatalogView<T> getCatalog() {
		return catalogView;
	}
}
