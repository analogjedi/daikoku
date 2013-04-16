package com.primateer.daikoku.model.vos;

import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.model.Amount.UnitConversionException;

public class Nutrition extends ValueObject<Nutrition> {
	
	private static final Amount defaultAmount = new Amount(100,"g");

	private Amount referenceAmount;
	private Map<String, Amount> nutrients;
	
	
	public Amount getReferenceAmount() {
		if (referenceAmount == null) {
			return defaultAmount;
		}
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
		return nutrients.get(type).scale(amount.divideBy(getReferenceAmount()));
	}
}
