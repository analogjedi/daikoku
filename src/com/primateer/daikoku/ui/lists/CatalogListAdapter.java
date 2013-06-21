package com.primateer.daikoku.ui.lists;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.primateer.daikoku.Event;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.ValueObject;

public class CatalogListAdapter<T extends ValueObject> extends
		DataRowListAdapter<T> {

	public CatalogListAdapter(Catalog<T> catalog) {
		super();
		this.data = catalog;
		catalog.reload();
	}

	@Override
	public void setData(List<T> data) {
		if (!(data instanceof Catalog)) {
			throw new IllegalArgumentException(
					"CatalogListAdapter only works with Catalogs");
		}
	}

	@Override
	protected DataRowWidget<T> newWidget(Context context) {
		final CatalogRowWidget<T> widget = new CatalogRowWidget<T>(context);
		if (getCatalog().isEditable()) {
			widget.setupForm(getCatalog().dataClass);
		}
		widget.addEventListener(CatalogRowWidget.SelectedEvent.class,
				new Event.Listener() {
					@Override
					public void onEvent(Event event) {
						getCatalog().select(widget.getRowData());
					}
				});
		return widget;
	}

	public Catalog<T> getCatalog() {
		return (Catalog<T>) data;
	}

	public void reload() {
		getCatalog().reload();
		super.notifyObservers();
	}

	@Override
	public void add(T item) {
		DBController.getInstance().register(item);
		super.add(item);
	}

	@Override
	public void onDelete(View v) {
		Helper.deleteOnConfirmation(v.getContext(), getItemFromView(v));
	}

	@Override
	public void remove(T item) {
		DBController.getInstance().delete(item);
		super.remove(item);
	}
}
