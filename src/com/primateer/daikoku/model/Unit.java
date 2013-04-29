package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.Map;

public class Unit {
	
	public enum Type {
		UNSPECIFIED, COUNT, CURRENCY, ENERGY, MASS, VOLUME;
	}

	public static final Unit UNIT_UNITS = new Unit("units",Type.COUNT);
	public static final Unit UNIT_EURO = new Unit("€",Type.CURRENCY);
	public static final Unit UNIT_DOLLAR = new Unit("$",Type.CURRENCY);
	public static final Unit UNIT_GRAM = new Unit("g", Type.MASS);
	public static final Unit UNIT_MILLIGRAM = new Unit("mg", Type.MASS);
	public static final Unit UNIT_KILOGRAM = new Unit("kg", Type.MASS);
	public static final Unit UNIT_POUND = new Unit("lb", Type.MASS);
	public static final Unit UNIT_KILOCALORIE = new Unit("kcal", Type.ENERGY);
	public static final Unit UNIT_MILLILITER = new Unit("ml", Type.VOLUME);

	static {
		UNIT_MILLIGRAM.setConversionRate(UNIT_GRAM, 0.001);
		UNIT_KILOGRAM.setConversionRate(UNIT_GRAM, 1000.0);
		UNIT_POUND.setConversionRate(UNIT_MILLIGRAM, 453592.37);
		UNIT_POUND.setConversionRate(UNIT_GRAM, 453.59237);
		UNIT_POUND.setConversionRate(UNIT_KILOGRAM, 0.45359237);
	}

	public final String symbol;
	public final Type type;

	private Map<Unit, Double> conversionRates = new HashMap<Unit, Double>();

	private Unit(String symbol, Type type) {
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
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
