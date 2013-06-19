package com.primateer.daikoku.model;

public class ValueObject {
	private long id = -1;

	public long getId() {
		return id;
	}

	public long setId(long id) {
		this.id = id;
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ValueObject)) {
			return false;
		}
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		ValueObject other = (ValueObject) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
