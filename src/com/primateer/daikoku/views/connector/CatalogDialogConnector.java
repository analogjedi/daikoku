package com.primateer.daikoku.views.connector;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.CatalogView;

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
		final Observer<Class<ValueObject>> dbObserver = new Observer<Class<ValueObject>>() {
			@Override
			public void update(Class<ValueObject> observable) {
				if (observable.equals(catalog.dataClass)) {
					catalogView.reload();
				}
			}
		};
		DBController.getInstance().addObserver(dbObserver);
		catalogView = new CatalogView<T>(context, catalog);
		dialog = new Dialog(context);
		dialog.setTitle(title);
		ViewGroup content = (ViewGroup) catalogView;
		content.setBackgroundColor(Color.WHITE); // FIXME
		dialog.setContentView(content);
		catalog.addObserver(new Observer<T>() {
			@Override
			public void update(T observable) {
				dialog.dismiss();
				DBController.getInstance().removeObserver(dbObserver);
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
