package com.primateer.daikoku.views;


public interface Catalog<T> {

	void add(T item);
	void setDataClass(Class<T> dataClass);
}
