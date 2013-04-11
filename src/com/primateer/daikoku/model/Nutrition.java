package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.pojos.Amount;
import com.primateer.daikoku.pojos.Amount.UnitConversionException;

public class Nutrition {

	private static final Amount DEFAULT_AMOUNT = new Amount("100g");

	private long id;
	private Amount referenceAmount;
	private Map<String, Amount> nutrients;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Amount getReferenceAmount() {
		return referenceAmount;
	}

	public void setReferenceAmount(Amount referenceAmount) {
		this.referenceAmount = referenceAmount;
	}

	public void setNutrients(Map<String, Amount> nutrients) {
		this.nutrients = nutrients;
	}

	public Nutrition setNutrient(String type, Amount amount) {
		if (nutrients == null) {
			nutrients = new HashMap<String,Amount>();
		}
		if (amount == null) {
			nutrients.remove(type);
		} else {
			nutrients.put(type, amount);
		}
		return this;
	}

	public Amount getAmount() {
		return referenceAmount;
	}

	public void setAmount(Amount amount) {
		this.referenceAmount = amount;
	}

	public Map<String, Amount> getNutrients() {
		return nutrients;
	}

	public Amount get(String type, Amount amount)
			throws UnitConversionException {
		Amount base = nutrients.get(type);
		if (base != null) {
			return base.scale(amount.divideBy(referenceAmount));
		}
		return null;
	}
}
