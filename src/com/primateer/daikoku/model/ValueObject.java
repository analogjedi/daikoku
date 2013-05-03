package com.primateer.daikoku.model;

public class ValueObject extends SimpleObservable<ValueObject> {
	private long id = -1;

	public long getId() {
		return id;
	}

	public long setId(long id) {
		this.id = id;
		return id;
	}
}
