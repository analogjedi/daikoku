package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NutrientSet extends ArrayList<Nutrient> {

	private Map<Nutrient.Type, Nutrient> byType = new HashMap<Nutrient.Type, Nutrient>();

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

	public Nutrient get(Nutrient.Type type) {
		Nutrient nutrient = byType.get(type);
		if (nutrient == null) {
			// TODO implement "minimum, open end" amounts for unknown nutrients
			return new Nutrient(type, new Amount(0,type.defaultUnit));
		}
		return byType.get(type);
	}
}
