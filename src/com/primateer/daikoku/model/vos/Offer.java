package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.ValueObject;

public class Offer extends ValueObject {
	
	Product product;
	Vendor vendor;
	Amount price;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public Amount getPrice() {
		return price;
	}
	public void setPrice(Amount price) {
		this.price = price;
	}
}
