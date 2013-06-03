package com.primateer.daikoku.ui.views.connector;

import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.views.CatalogView;

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

		LinearLayout tabs = new LinearLayout(context);
		tabs.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout pages = new LinearLayout(context);

		boolean firstView = true;
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
			CatalogView view = new CatalogView(context, catalog);
			view.setVisibility(firstView ? View.VISIBLE : View.GONE);
			firstView = false;
			views.put(catalog, view);
			catalog.addObserver(new Observer() {
				@Override
				public void update(Object observable) {
					dialog.dismiss();
					DBController.getInstance().removeEventListener(
							DBController.DBChangedEvent.class, dbListener);
					dialog = null;
				}
			});

			Button tab = new Button(context);
			tab.setText(catalog.getTitle());
			tab.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (Catalog<?> cat : views.keySet()) {
						views.get(cat).setVisibility(View.GONE);
					}
					views.get(catalog).setVisibility(View.VISIBLE);
				}
			});
			LinearLayout.LayoutParams tabLayout = new LinearLayout.LayoutParams(
					0, LayoutParams.WRAP_CONTENT, 1.0f);
			tabs.addView(tab, tabLayout);

			pages.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
		}

		dialog = new Dialog(context);
		dialog.setTitle(title);
		LinearLayout content = new LinearLayout(context);
		content.setOrientation(LinearLayout.VERTICAL);
		content.addView(tabs, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		content.addView(pages, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 1.0f));
		content.setBackgroundColor(Color.WHITE); // FIXME
		dialog.setContentView(content);
	}

	public void showDialog() {
		dialog.show();
	}
}
