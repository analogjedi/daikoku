package com.primateer.daikoku.actions;

import com.primateer.daikoku.model.Catalog;

public class CatalogAction implements Action {
	
	private Catalog<?> catalog;
	
	public CatalogAction(Catalog<?> catalog) {
		this.catalog = catalog;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
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
