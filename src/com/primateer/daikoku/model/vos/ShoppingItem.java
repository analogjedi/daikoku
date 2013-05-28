package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.ValueObject;

public class ShoppingItem extends ValueObject {

	public final Product product;
	private Amount amount;
	private boolean checked;

	public ShoppingItem(Product product) {
		this(product, new Amount(0, product.getAmount().unit), false);
	}

	public ShoppingItem(Product product, Amount amount, boolean checked) {
		this.product = product;
		this.amount = amount;
		this.checked = checked;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Product getProduct() {
		return product;
	}
}
