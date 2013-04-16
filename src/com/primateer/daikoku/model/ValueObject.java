package com.primateer.daikoku.model;

public class ValueObject<T> extends Observable<T> {
	private long id = -1;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
