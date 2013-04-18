package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.Map;

public class Unit {

	public static final int TYPE_UNSPECIFIED = 0;
	public static final int TYPE_CURRENCY = 1;
	public static final int TYPE_ENERGY = 2;
	public static final int TYPE_MASS = 3;
	public static final int TYPE_VOLUME = 4;

	public static final Unit UNIT_EURO = new Unit("€",TYPE_CURRENCY);
	public static final Unit UNIT_DOLLAR = new Unit("$",TYPE_CURRENCY);
	public static final Unit UNIT_GRAM = new Unit("g", TYPE_MASS);
	public static final Unit UNIT_KILOGRAM = new Unit("kg", TYPE_MASS);
	public static final Unit UNIT_POUND = new Unit("lb", TYPE_MASS);
	public static final Unit UNIT_KILOCALORIE = new Unit("kcal", TYPE_ENERGY);
	public static final Unit UNIT_MILLILITER = new Unit("ml", TYPE_VOLUME);

	static {
		UNIT_KILOGRAM.setConversionRate(UNIT_GRAM, 1000.0);
		UNIT_POUND.setConversionRate(UNIT_GRAM, 453.59237);
		UNIT_POUND.setConversionRate(UNIT_KILOGRAM, 0.45359237);
	}

	public final String symbol;
	public final int type;

	private Map<Unit, Double> conversionRates = new HashMap<Unit, Double>();

	private Unit(String symbol, int type) {
		this.symbol = symbol;
		this.type = type;
	}

	public Double getConversionRate(Unit target) {
		return this.conversionRates.get(target);
	}

	public void setConversionRate(Unit target, Double rate) {
		this.conversionRates.put(target, rate);
		if (rate == null) {
			if (target.getConversionRate(this) != null) {
				target.setConversionRate(this, null);
			}
		} else {
			Double targetRate = target.getConversionRate(this);
			if (targetRate == null || targetRate != 1 / rate) {
				target.setConversionRate(this, 1 / rate);
			}
		}
	}

	@Override
	public String toString() {
		return symbol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + type;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unit other = (Unit) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
