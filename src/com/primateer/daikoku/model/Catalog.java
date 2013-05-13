package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.Collection;

import com.primateer.daikoku.db.Database;

public class Catalog<T extends ValueObject> extends ArrayList<T> implements
		Observable<T> {

	public interface Loader<T extends ValueObject> {
		public Collection<T> load(Catalog<T> catalog);
	}

	private SimpleObservable<T> selectionObservable = new SimpleObservable<T>();
	public final Class<T> dataClass;
	private Loader<T> loader;

	public Catalog(final Class<T> dataClass) {
		this.dataClass = dataClass;
		this.setLoader(new Loader<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public Collection<T> load(Catalog<T> cat) {
				return (Collection<T>) Database.getInstance().loadAll(dataClass);
			}
		});
	}

	public void setLoader(Loader<T> loader) {
		this.loader = loader;
	}

	public void select(T item) {
		selectionObservable.notifyObservers(item);
	}

	public void reload() {
		this.clear();
		if (loader != null) {
			this.addAll(loader.load(this));
		}
	}

	@Override
	public void addObserver(Observer<T> observer) {
		selectionObservable.addObserver(observer);
	}

	@Override
	public void removeObserver(Observer<T> observer) {
		selectionObservable.removeObserver(observer);
	}
}
