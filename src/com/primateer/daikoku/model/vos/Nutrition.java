package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.NutrientSet;
import com.primateer.daikoku.model.ValueObject;

public class Nutrition extends ValueObject<Nutrition> {

	private Amount referenceAmount;
	private NutrientSet nutrients;

	public Amount getReferenceAmount() {
		if (referenceAmount == null) {
			return NutrientRegistry.getInstance().getDefaultReferenceAmount();
		}
		return referenceAmount;
	}

	public void setReferenceAmount(Amount referenceAmount) {
		this.referenceAmount = referenceAmount;
	}

	public NutrientSet getNutrients() {
		return nutrients;
	}

	public void setNutrients(NutrientSet nutrients) {
		this.nutrients = nutrients;
	}

	public Nutrition setNutrient(Nutrient nutrient) {
		if (nutrients == null) {
			nutrients = new NutrientSet();
		}
		if (nutrient.amount == null) {
			nutrients.remove(nutrient.type);
		} else {
			nutrients.add(nutrient);
		}
		return this;
	}

	public Amount getAmount(Nutrient.Type type, Amount multiplier)
			throws UnitConversionException {
		return nutrients.get(type).amount.scale(multiplier
				.divideBy(getReferenceAmount()));
	}
}
