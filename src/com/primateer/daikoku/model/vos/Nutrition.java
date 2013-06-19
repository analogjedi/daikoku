package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.AmountException;
import com.primateer.daikoku.model.Amount.UnknownAmountException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.NutrientSet;
import com.primateer.daikoku.model.ValueObject;

public class Nutrition extends ValueObject {

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
			throws AmountException {
		try {
			return nutrients.get(type).amount.scale(multiplier
					.divideBy(getReferenceAmount()));
		} catch (UnknownAmountException e) {
			return type.getNullAmount();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nutrients == null) ? 0 : nutrients.hashCode());
		result = prime * result
				+ ((referenceAmount == null) ? 0 : referenceAmount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Nutrition other = (Nutrition) obj;
		if (nutrients == null) {
			if (other.nutrients != null)
				return false;
		} else if (!nutrients.equals(other.nutrients))
			return false;
		if (referenceAmount == null) {
			if (other.referenceAmount != null)
				return false;
		} else if (!referenceAmount.equals(other.referenceAmount))
			return false;
		return true;
	}
}
