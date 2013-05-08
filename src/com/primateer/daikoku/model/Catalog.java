package com.primateer.daikoku.model;

import java.util.ArrayList;

public class Catalog<T extends ValueObject> extends ArrayList<T> {

	public interface SelectionListener<T> {
		public void onSelection(T item);
	}
	
	public interface Loader<T extends ValueObject> {
		public void load(Catalog<T> catalog);		
	}

	public final Class<T> dataClass;
	private SelectionListener<T> listener;
	private Loader<T> loader;

	public Catalog(Class<T> dataClass) {
		this.dataClass = dataClass;
	}

	public void setSelectionListener(SelectionListener<T> listener) {
		this.listener = listener;
	}
	
	public void setLoader(Loader<T> loader) {
		this.loader = loader;
	}

	public void select(T item) {
		// TODO check if item is contained
		if (listener != null) {
		listener.onSelection(item);
		}
	}
	
	public void reload() {
		this.clear();
		if (loader != null) {
			loader.load(this);
		}
	}
}
