package com.primateer.daikoku.model;

public interface Observable<T> {

	void addObserver(Observer<T> observer);
	void removeObserver(Observer<T> observer);
}
