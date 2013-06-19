package com.primateer.daikoku.ui.views.connector;

import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.ui.views.CatalogView;
import com.primateer.daikoku.ui.views.TabLayout;

public class MultiCatalogDialogConnector {

	private HashMap<Catalog<?>, CatalogView<?>> views = new HashMap<Catalog<?>, CatalogView<?>>();
	private Dialog dialog;

	public MultiCatalogDialogConnector(List<Catalog<?>> catalogs,
			View launcher, String title) {
		this(catalogs, launcher.getContext(), title);
		launcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MultiCatalogDialogConnector.this.showDialog();
			}
		});
	}

	public MultiCatalogDialogConnector(final List<Catalog<?>> catalogs,
			Context context, String title) {

		dialog = new Dialog(context);
		dialog.setTitle(title);

		TabLayout content = new TabLayout(context);
		for (final Catalog<?> catalog : catalogs) {
			final Event.Listener dbListener = new Event.Listener() {
				@Override
				public void onEvent(Event event) {
					DBController.DBChangedEvent e = (DBController.DBChangedEvent) event;
					if (e.type.equals(catalog.dataClass)) {
						views.get(catalog).reload();
					}
				}
			};
			DBController.getInstance().addEventListener(
					DBController.DBChangedEvent.class, dbListener);
			catalog.addEventListener(Catalog.SelectionEvent.class,
					new Event.Listener() {
						@Override
						public void onEvent(Event event) {
							dialog.dismiss();
							DBController.getInstance().removeEventListener(
									DBController.DBChangedEvent.class,
									dbListener);
							dialog = null;
						}
					});
			@SuppressWarnings({ "unchecked", "rawtypes" })
			CatalogView view = new CatalogView(context, catalog);
			content.addPage(catalog.getTitle(), view);
		}

		content.setBackgroundColor(Color.WHITE); // FIXME
		dialog.setContentView(content);
	}

	public void showDialog() {
		dialog.show();
	}
}
