package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Amount.UnknownAmountException;

public class NutrientSet extends ArrayList<Nutrient> {

	private Map<Nutrient.Type, Nutrient> byType = new HashMap<Nutrient.Type, Nutrient>();

	public void absorb(NutrientSet other) {
		for (Nutrient otherNutrient : other) {
			Nutrient ourNutrient = byType.get(otherNutrient.type);
			if (ourNutrient == null) {
				this.add(otherNutrient);
			} else {
				try {
					this.set(
							this.indexOf(ourNutrient),
							new Nutrient(otherNutrient.type, ourNutrient.amount
									.add(otherNutrient.amount)));
				} catch (UnitConversionException e) {
					throw new RuntimeException(e); // should be unreachable
				}
			}
		}
	}

	@Override
	public boolean add(Nutrient nutrient) {
		if (super.contains(nutrient)) {
			return false;
		}
		if (isTypeOccupied(nutrient.type)) {
			throw new IllegalArgumentException("Cannot add " + nutrient.type
					+ ": already occupied");
		}
		super.add(nutrient);
		byType.put(nutrient.type, nutrient);
		return true;
	}

	@Override
	public void clear() {
		super.clear();
		byType.clear();
	}

	@Override
	public boolean remove(Object object) {
		if (super.remove(object)) {
			byType.remove(((Nutrient) object).type);
			return true;
		}
		return false;
	}

	public boolean isTypeOccupied(Nutrient.Type type) {
		return byType.containsKey(type);
	}

	public Nutrient get(Nutrient.Type type) throws UnknownAmountException {
		Nutrient nutrient = byType.get(type);
		if (nutrient == null) {
			throw new UnknownAmountException("Unknown Amount: Type not listed");
		}
		return byType.get(type);
	}
}
