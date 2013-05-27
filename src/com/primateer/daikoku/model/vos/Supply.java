package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.ValueObject;

public class Supply extends ValueObject {

	public final Product product;
	Amount total;
	Amount available;
	Amount reserved;
	Amount consumed;
	Amount cost;

	public Supply(Product product, Amount total) {
		this(product, total.unit, total.value, total.value, 0, 0, new Amount(0,
				UnitRegistry.getInstance().getDefaultUnitByType(
						Unit.Type.CURRENCY)));
	}

	public Supply(Product product, Unit unit, double total, double available,
			double reserved, double consumed, Amount cost) {
		this.product = product;
		if (available + reserved + consumed != total) {
			throw new IllegalArgumentException("Supply amounts do not add up");
		}
		this.total = new Amount(total, unit);
		this.available = new Amount(available, unit);
		this.reserved = new Amount(reserved, unit);
		this.consumed = new Amount(consumed, unit);
		this.cost = cost;
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

	public Amount getCost() {
		return cost;
	}
}
