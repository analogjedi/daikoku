package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.model.ValueObject;

public class Supply extends ValueObject {

	double total;
	double available;
	double reserved;
	double consumed;
	
	
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getAvailable() {
		return available;
	}
	public void setAvailable(double available) {
		this.available = available;
	}
	public double getReserved() {
		return reserved;
	}
	public void setReserved(double reserved) {
		this.reserved = reserved;
	}
	public double getConsumed() {
		return consumed;
	}
	public void setConsumed(double consumed) {
		this.consumed = consumed;
	}
	
	
}
