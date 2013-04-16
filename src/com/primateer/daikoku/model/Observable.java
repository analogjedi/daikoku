package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

	private final List<Observer<T>> observers = new ArrayList<Observer<T>>();

	public void addObserver(Observer<T> observer) {
		synchronized (observers) {
			observers.add(observer);
		}
	}

	public void removeObserver(Observer<T> observer) {
		synchronized (observers) {
			observers.remove(observer);
		}
	}

	protected void notifyObservers(final T object) {
		synchronized (observers) {
			for (Observer<T> observer : observers) {
				observer.update(object);
			}
		}
	}
}
