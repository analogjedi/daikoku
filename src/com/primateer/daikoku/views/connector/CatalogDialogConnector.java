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
import com.primateer.daikoku.views.ListCatalog;

public class CatalogDialogConnector<T extends ValueObject> implements
		Catalog<T>, Connector<T> {

	private Class<T> dataClass;
	private Context context;
	private Catalog<T> catalog;
	private Dialog dialog;
	private Observer<T> selectionObserver;

	@Override
	public void showDialog() {
		if (dialog == null) {
			catalog = new ListCatalog<T>(context);
			catalog.setDataClass(dataClass);
			setSelectionObserver(selectionObserver);
			dialog = new Dialog(context);
			dialog.setTitle("PRODUCT CATALOG"); // FIXME
			ViewGroup content = (ViewGroup) catalog;
			content.setBackgroundColor(Color.WHITE); // FIXME
			dialog.setContentView(content);
		}
		dialog.show();
	}

	@Override
	public void register(Class<T> dataClass, View launcher) {
		register(dataClass, launcher.getContext());
		launcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CatalogDialogConnector.this.showDialog();
			}
		});
	}

	@Override
	public void register(Class<T> dataClass, Context context) {
		this.context = context;
		setDataClass(dataClass);
	}

	@Override
	public void add(T item) {
		catalog.add(item);
	}

	@Override
	public void setDataClass(Class<T> dataClass) {
		this.dataClass = dataClass;
	}

	@Override
	public View getView() {
		return catalog.getView();
	}

	@Override
	public void setSelectionObserver(Observer<T> observer) {
		this.selectionObserver = observer;
		if (catalog != null) {
			catalog.setSelectionObserver(new Observer<T>() {
				@Override
				public void update(T item) {
					dialog.dismiss();
					dialog = null;
					selectionObserver.update(item);
				}
			});
		}
	}

}
