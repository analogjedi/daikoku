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

	private Class<T> dataClass;
	private Context context;
	private Catalog<T> catalog;
	private Dialog dialog;
	private Observer<T> selectionObserver;
	private String title;

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
			Observer<T> selectionObserver, String title) {
		this.title = title;
		this.context = context;
		this.dataClass = dataClass;
		this.selectionObserver = selectionObserver;
	}

	public void showDialog() {
		if (dialog == null) {
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
		dialog.show();
	}

	public void add(T item) {
		catalog.add(item);
	}

	public View getView() {
		return catalog;
	}
}
