package com.primateer.daikoku.views.connector;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.Catalog;

public class CatalogDialogConnector<T extends ValueObject> {

	private Catalog<T> catalog;
	private Dialog dialog;

	public CatalogDialogConnector(Class<T> dataClass, View launcher,
			Observer<T> selectionObserver, String title) {
		this(dataClass, launcher.getContext(), selectionObserver, title);
		launcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CatalogDialogConnector.this.showDialog();
			}
		});
	}

	public CatalogDialogConnector(Class<T> dataClass, Context context,
			final Observer<T> selectionObserver, String title) {
		catalog = new Catalog<T>(context, dataClass, new Observer<T>() {
			@Override
			public void update(T item) {
				dialog.dismiss();
				dialog = null;
				selectionObserver.update(item);
			}
		});
		dialog = new Dialog(context);
		dialog.setTitle(title);
		ViewGroup content = (ViewGroup) catalog;
		content.setBackgroundColor(Color.WHITE); // FIXME
		dialog.setContentView(content);
	}

	public void showDialog() {
		dialog.show();
	}

	public Catalog<T> getCatalog() {
		return catalog;
	}
}
