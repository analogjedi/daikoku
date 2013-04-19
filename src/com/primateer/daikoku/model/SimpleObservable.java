package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.List;

public class SimpleObservable<T> implements Observable<T> {

	private final List<Observer<T>> observers = new ArrayList<Observer<T>>();

	@Override
	public void addObserver(Observer<T> observer) {
		synchronized (observers) {
			observers.add(observer);
		}
	}

	@Override
	public void removeObserver(Observer<T> observer) {
		synchronized (observers) {
			observers.remove(observer);
		}
	}

	public void notifyObservers(final T object) {
		synchronized (observers) {
			for (Observer<T> observer : observers) {
				observer.update(object);
			}
		}
	}
}
