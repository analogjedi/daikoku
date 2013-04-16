package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.model.ValueObject;

public class Vendor extends ValueObject<Vendor> {

	String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
