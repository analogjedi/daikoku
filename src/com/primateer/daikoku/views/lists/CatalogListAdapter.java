package com.primateer.daikoku.views.lists;

import android.content.Context;
import android.view.View;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.actions.Action;
import com.primateer.daikoku.actions.DeleteDataAction;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.widgets.row.CatalogRowWidget;
import com.primateer.daikoku.views.widgets.row.DataRowWidget;

public class CatalogListAdapter<T extends ValueObject> extends
		DataRowListAdapter<T> {

	private Class<T> dataClass;
	private Observer<T> selectionObserver;

	public CatalogListAdapter(Class<T> dataClass, Observer<T> selectionObserver) {
		super();
		this.dataClass = dataClass;
		this.selectionObserver = selectionObserver;
	}

	@Override
	protected DataRowWidget<T> newWidget(Context context) {
		CatalogRowWidget<T> widget = new CatalogRowWidget<T>(context);
		widget.setDataClass(dataClass);
		widget.setSelectionObserver(selectionObserver);
		return widget;
	}

	@Override
	public void onClick(final View v) {
		Action action = new DeleteDataAction<T>(getItemFromView(v),
				v.getContext());
		Application.getInstance().dispatch(action);
	}
}