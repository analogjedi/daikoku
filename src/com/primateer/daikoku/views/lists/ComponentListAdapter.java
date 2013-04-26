package com.primateer.daikoku.views.lists;

import android.content.Context;

import com.primateer.daikoku.model.Component;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.views.widgets.row.ComponentRowWidget;
import com.primateer.daikoku.views.widgets.row.DataRowWidget;

public class ComponentListAdapter extends DataRowListAdapter<Component> {

	@Override
	protected DataRowWidget<Component> newWidget(Context context) {
		return new ComponentRowWidget(context);
	}

	public void add(Product item) {
		super.add(new Component(item));
	}

}
