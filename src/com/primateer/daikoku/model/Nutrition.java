package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.pojos.Amount;
import com.primateer.daikoku.pojos.Amount.UnitConversionException;

public class Nutrition {

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

	public Map<String, Amount> getNutrients() {
		return nutrients;
	}

	public Amount get(String type, Amount amount)
			throws UnitConversionException {
		return nutrients.get(type).scale(amount.divideBy(referenceAmount));
	}
}
