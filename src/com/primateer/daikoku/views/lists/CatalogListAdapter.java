package com.primateer.daikoku.views.lists;

import android.content.Context;

import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.widgets.CatalogRowWidget;
import com.primateer.daikoku.views.widgets.DataRowWidget;

public class CatalogListAdapter<T extends ValueObject<T>> extends
		DataRowListAdapter<T> {

	@Override
	protected DataRowWidget<T> newWidget(Context context) {
		return new CatalogRowWidget<T>(context);
	}
}
