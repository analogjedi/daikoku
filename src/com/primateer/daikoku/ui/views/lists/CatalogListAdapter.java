package com.primateer.daikoku.ui.views.lists;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.actions.DeleteDataAction;
import com.primateer.daikoku.ui.actions.SaveDataAction;
import com.primateer.daikoku.ui.views.widgets.row.CatalogRowWidget;
import com.primateer.daikoku.ui.views.widgets.row.DataRowWidget;

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
		CatalogRowWidget<T> widget = new CatalogRowWidget<T>(context);
		widget.setDataClass(((Catalog<T>) data).dataClass);
		widget.setSelectionObserver(new Observer<T>() {
			@Override
			public void update(T item) {
				((Catalog<T>) data).select(item);
			}
		});
		return widget;
	}

	public Catalog<T> getCatalog() {
		return (Catalog<T>) data;
	}

	public void reload() {
		((Catalog<T>) data).reload();
		super.notifyObservers();
	}

	@Override
	public void add(T item) {
		Application.getInstance().dispatch(new SaveDataAction<T>(item));
		super.add(item);
	}

	@Override
	public void onClick(View v) {
		Application.getInstance().dispatch(new DeleteDataAction<T>(getItemFromView(v),v.getContext()));
	}
	
	@Override
	public void remove(T item) {
		Application.getInstance().dispatch(new DeleteDataAction<T>(item, null));
		super.remove(item);
	}
}
