package com.primateer.daikoku.model;

public interface Observer<T> {

	public void update(T observable);
}
