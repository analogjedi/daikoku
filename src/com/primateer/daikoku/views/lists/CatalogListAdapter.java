package com.primateer.daikoku.views.lists;

import android.content.Context;

import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.widgets.CatalogRowWidget;
import com.primateer.daikoku.views.widgets.DataRowWidget;

public class CatalogListAdapter<T extends ValueObject<T>> extends
		DataRowListAdapter<T> {
	
	private Class<T> dataClass;

	@Override
	protected DataRowWidget<T> newWidget(Context context) {
		CatalogRowWidget<T> widget = new CatalogRowWidget<T>(context);
		widget.setDataClass(dataClass);
		return widget;
	}

	public void setClass(Class<T> dataClass) {
		this.dataClass = dataClass;
	}
}
