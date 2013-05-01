package com.primateer.daikoku.model;

public class ValueObject extends SimpleObservable<ValueObject> {
	private long id = -1;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
