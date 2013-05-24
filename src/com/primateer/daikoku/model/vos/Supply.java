package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.ValueObject;

public class Supply extends ValueObject {

	public final Product product;
	Amount total;
	Amount available;
	Amount reserved;
	Amount consumed;
	
	public Supply(Product product, Amount total) {
		this.product = product;
		this.total = total;
		this.available = new Amount(total);
		this.reserved = new Amount(0,total.unit);
		this.consumed = new Amount(0,total.unit);
	}

	public Amount getTotal() {
		return total;
	}

	public Amount getAvailable() {
		return available;
	}

	public Amount getReserved() {
		return reserved;
	}

	public Amount getConsumed() {
		return consumed;
	}
	
}
