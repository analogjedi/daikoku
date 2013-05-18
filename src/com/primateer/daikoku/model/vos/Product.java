package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.AmountException;
import com.primateer.daikoku.model.Nutrient.Type;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.ValueObject;

public class Product extends ValueObject {

	private String label;
	private Nutrition nutrition;
	private Amount amount;
	private double units;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Nutrition getNutrition() {
		return nutrition;
	}

	public void setNutrition(Nutrition nutrition) {
		this.nutrition = nutrition;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public double getUnits() {
		return units;
	}
	
	public Amount getUnitsAmount() {
		return new Amount(units,Unit.UNIT_UNITS);
	}
	
	public Amount getAmountPerUnit() {
		if (units <= 0) {
			return null;
		}
		return new Amount(amount.value / units, amount.unit);
	}

	public void setUnits(double units) {
		this.units = units;
	}

	public void setUnits(Amount units) {
		if (units.unit.type != Unit.Type.COUNT) {
			throw new IllegalArgumentException(
					"Units must be of type TYPE_COUNT");
		}
		setUnits(units.value);
	}

	@Override
	public String toString() {
		if (Helper.isEmpty(label)) {
			return Application.getContext().getResources()
					.getString(R.string.placeholder_empty);
		}
		return label;
	}

	public Amount getDefaultAmount() {
		if (units > 0) {
			return amount.scale(1/units);
		}
		return amount;
	}

	public Amount getNutrition(Type type) throws AmountException {
		return nutrition.getAmount(type, amount);
	}
}
