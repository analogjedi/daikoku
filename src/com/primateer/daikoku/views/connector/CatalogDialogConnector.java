package com.primateer.daikoku.views.connector;

import java.util.Collection;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Catalog.SelectionListener;
import com.primateer.daikoku.model.Catalog.Loader;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.CatalogView;

public class CatalogDialogConnector<T extends ValueObject> {

	private CatalogView<T> catalogView;
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

	public CatalogDialogConnector(final Class<T> dataClass, Context context,
			final Observer<T> selectionObserver, String title) {
		final Observer<Class<ValueObject>> dbObserver = new Observer<Class<ValueObject>>() {
			@Override
			public void update(Class<ValueObject> observable) {
				if (observable.equals(dataClass)) {
					catalogView.reload();
				}
			}
		};
		Data.getInstance().addObserver(dbObserver);
		Catalog<T> catalog = new Catalog<T>(dataClass);
		catalog.setLoader(new Loader<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public void load(Catalog<T> cat) {
				cat.addAll((Collection<? extends T>) Data.getInstance()
						.getAll(dataClass));
			}
		});
		catalog.setSelectionListener(new SelectionListener<T>() {
			@Override
			public void onSelection(T item) {
				dialog.dismiss();
				Data.getInstance().removeObserver(dbObserver);
				dialog = null;
				selectionObserver.update(item);
			}
		});
		catalogView = new CatalogView<T>(context, catalog);
		dialog = new Dialog(context);
		dialog.setTitle(title);
		ViewGroup content = (ViewGroup) catalogView;
		content.setBackgroundColor(Color.WHITE); // FIXME
		dialog.setContentView(content);
	}

	public void showDialog() {
		dialog.show();
	}

	public CatalogView<T> getCatalog() {
		return catalogView;
	}
}
