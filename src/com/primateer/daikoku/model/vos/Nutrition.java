package com.primateer.daikoku.model.vos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Settings;
import com.primateer.daikoku.model.ValueObject;

public class Nutrition extends ValueObject<Nutrition> {

	private Amount referenceAmount;
	private Map<Nutrient.Type, Nutrient> nutrients;

	public Amount getReferenceAmount() {
		if (referenceAmount == null) {
			return Settings.getInstance().getDefaultReferenceAmount();
		}
		return referenceAmount;
	}

	public void setReferenceAmount(Amount referenceAmount) {
		this.referenceAmount = referenceAmount;
	}

	public void setNutrients(List<Nutrient> nutrients) {
		this.nutrients = new HashMap<Nutrient.Type, Nutrient>();
		for (Nutrient nutrient : nutrients) {
			this.nutrients.put(nutrient.type, nutrient);
		}
	}

	public void setNutrients(Map<Nutrient.Type, Nutrient> nutrients) {
		this.nutrients = nutrients;
	}

	public Nutrition setNutrient(Nutrient nutrient) {
		if (nutrients == null) {
			nutrients = new HashMap<Nutrient.Type, Nutrient>();
		}
		if (nutrient.amount == null) {
			nutrients.remove(nutrient.type);
		} else {
			nutrients.put(nutrient.type, nutrient);
		}
		return this;
	}

	public Map<Nutrient.Type, Nutrient> getNutrients() {
		return nutrients;
	}

	public Amount getAmount(String type, Amount multiplier)
			throws UnitConversionException {
		return nutrients.get(type).amount.scale(multiplier
				.divideBy(getReferenceAmount()));
	}
}
