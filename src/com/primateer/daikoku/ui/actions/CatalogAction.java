package com.primateer.daikoku.ui.actions;

import android.content.Context;

import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.dialogs.CatalogView;
import com.primateer.daikoku.ui.views.connector.DialogConnector;

public class CatalogAction<T extends ValueObject> implements Action {

	private Context context;
	private Catalog<T> catalog;
	private String title;

	public CatalogAction(Context context, Catalog<T> catalog, String title) {
		this.context = context;
		this.catalog = catalog;
		this.title = title;
	}

	@Override
	public void run() {
		DialogConnector connector = new DialogConnector(
				new CatalogView<T>(context, catalog), context, title);
		connector.showDialog();
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public int getIcon() {
		return -1;
	}
}
