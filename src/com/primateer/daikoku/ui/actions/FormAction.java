package com.primateer.daikoku.ui.actions;

import android.content.Context;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Observable;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.ui.views.connector.FormDialogConnector;

public class FormAction<T> implements Action,
		Observable<T> {

	private T data;
	private Context context;
	private SimpleObservable<T> observable = new SimpleObservable<T>();

	public FormAction(Context context, T data) {
		this.context = context;
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		final FormDialogConnector<T> formConnector = new FormDialogConnector<T>(
				(Class<T>) data.getClass(), context);
		formConnector.setData(data);
		formConnector.addObserver(new Observer<T>() {
			@Override
			public void update(T item) {
				observable.notifyObservers(item);
			}
		});
		formConnector.showDialog();
	}

	@Override
	public int getIcon() {
		return Application.ICON_EDIT;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void addObserver(Observer<T> observer) {
		observable.addObserver(observer);
	}

	@Override
	public void removeObserver(Observer<T> observer) {
		observable.removeObserver(observer);
	}
}
