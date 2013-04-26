package com.primateer.daikoku.views;

import android.view.View;

import com.primateer.daikoku.model.Observer;


public interface Catalog<T> {

	void add(T item);
	void setDataClass(Class<T> dataClass);
	void setSelectionObserver(Observer<T> observer);
	View getView();
}
