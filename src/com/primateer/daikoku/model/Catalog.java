package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.Collection;

import com.primateer.daikoku.Event;
import com.primateer.daikoku.Event.Listener;
import com.primateer.daikoku.db.DBController;

public class Catalog<T extends ValueObject> extends ArrayList<T> implements
		Event.Registry {
	
	public static class SelectionEvent<T extends ValueObject> extends Event {
		public final T selection;
		public SelectionEvent(T selection) {
			this.selection = selection;
		}
	}
	
	public static class TitleChangedEvent extends Event {
		public final String title;
		public TitleChangedEvent(String title) {
			this.title = title;
		}
	}

	public interface Loader<T extends ValueObject> {
		public Collection<T> load(Catalog<T> catalog);
	}

	private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();
	public final Class<T> dataClass;
	private Loader<T> loader;
	private String title;
	private boolean editable = true;

	public Catalog(final Class<T> dataClass) {
		this.dataClass = dataClass;
		this.setLoader(new Loader<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public Collection<T> load(Catalog<T> cat) {
				return (Collection<T>) DBController.getInstance().loadAll(dataClass);
			}
		});
		this.title = dataClass.getSimpleName();
	}

	public void setLoader(Loader<T> loader) {
		this.loader = loader;
	}

	public void select(T item) {
		dispatcher.dispatch(new SelectionEvent<T>(item));
	}

	public void reload() {
		this.clear();
		if (loader != null) {
			this.addAll(loader.load(this));
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		dispatcher.dispatch(new TitleChangedEvent(title));
	}

	@Override
	public void addEventListener(Class<? extends Event> type, Listener listener) {
		dispatcher.addEventListener(type,listener);
	}

	@Override
	public void removeEventListener(Class<? extends Event> type,
			Listener listener) {
		dispatcher.removeEventListener(type,listener);
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
