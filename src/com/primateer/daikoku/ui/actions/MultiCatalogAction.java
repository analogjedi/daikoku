package com.primateer.daikoku.ui.actions;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.ui.views.connector.MultiCatalogDialogConnector;

public class MultiCatalogAction implements Action {

	private Context context;
	private List<Catalog<?>> catalogs = new ArrayList<Catalog<?>>();
	private String title;

	public MultiCatalogAction(Context context, String title) {
		this.context = context;
		this.title = title;
	}

	public void add(Catalog<?> catalog) {
		this.catalogs.add(catalog);
	}

	@Override
	public void run() {
		MultiCatalogDialogConnector connector = new MultiCatalogDialogConnector(
				catalogs, context, title);
		connector.showDialog();
	}

	@Override
	public boolean isReady() {
		return catalogs.size() > 0;
	}

	@Override
	public int getIcon() {
		return -1;
	}
}
