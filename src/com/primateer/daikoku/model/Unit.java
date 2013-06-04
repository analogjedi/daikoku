package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Unit extends ValueObject {

	public enum Type {
		UNSPECIFIED, COUNT, CURRENCY, ENERGY, MASS, VOLUME;
	}

	public static final Unit UNIT_UNITS = new Unit("units", Type.COUNT);
	public static final Unit UNIT_EURO = new Unit("€", Type.CURRENCY);
	public static final Unit UNIT_DOLLAR = new Unit("$", Type.CURRENCY);
	public static final Unit UNIT_KILOGRAM = new Unit("kg", Type.MASS);
	public static final Unit UNIT_GRAM = new Unit("g", Type.MASS);
	public static final Unit UNIT_MILLIGRAM = new Unit("mg", Type.MASS);
	public static final Unit UNIT_MICROGRAM = new Unit("µg", Type.MASS);
	public static final Unit UNIT_POUND = new Unit("lb", Type.MASS);
	public static final Unit UNIT_KILOCALORIE = new Unit("kcal", Type.ENERGY);
	public static final Unit UNIT_MILLILITER = new Unit("ml", Type.VOLUME);

	static {
		UNIT_KILOGRAM.setConversionRate(Unit.UNIT_GRAM, 1000.0);
		UNIT_MILLIGRAM.setConversionRate(Unit.UNIT_GRAM, 0.001);
		UNIT_MICROGRAM.setConversionRate(Unit.UNIT_GRAM, 0.000001);
		UNIT_POUND.setConversionRate(Unit.UNIT_GRAM, 453.59237);
	}

	public final String symbol;
	public final Type type;

	private Map<Unit, Double> conversionRates = new HashMap<Unit, Double>();

	private Unit(String symbol, Type type) {
		this.symbol = symbol;
		this.type = type;
	}

	public Double getConversionRate(Unit target) {
		if (this.conversionRates.containsKey(target)) {
			return this.conversionRates.get(target);
		}
		// simplified A* search
		List<Unit> path = new ArrayList<Unit>();
		path.add(this);
		Set<Unit> explored = new HashSet<Unit>();
		List<Unit> candidates = new ArrayList<Unit>(conversionRates.keySet());
		while (candidates.size() > 0) {
			Unit candidate = candidates.remove(0);
			path.add(candidate);
			explored.add(candidate);
			if (candidate.conversionRates.containsKey(target)) {
				path.add(target);
				double rate = 1.0;
				for (int i=0; i<path.size()-1; i++) {
					rate *= path.get(i).conversionRates.get(path.get(i+1));
				}
				return rate;
			}
			for (Unit node : candidate.conversionRates.keySet()) {
				if (!explored.contains(node)) {
					candidates.add(node);
				}
			}
			path.remove(candidate);
		}
		return null;
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
