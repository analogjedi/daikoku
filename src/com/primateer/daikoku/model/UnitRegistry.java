package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.primateer.daikoku.model.Unit.Type;

import android.util.SparseArray;

public class UnitRegistry {

	private static UnitRegistry instance;

	public static synchronized UnitRegistry getInstance() {
		if (instance == null) {
			instance = new UnitRegistry();
		}
		return instance;
	}

	private Map<String, Unit> bySymbol = new HashMap<String, Unit>();
	private SparseArray<Unit> defaultByType = new SparseArray<Unit>();
	private SparseArray<List<Unit>> byType = new SparseArray<List<Unit>>();;
	private List<Unit> all;

	private UnitRegistry() {
		this.register(Unit.UNIT_UNITS);
		this.setDefaultUnit(Unit.UNIT_UNITS);

		this.register(Unit.UNIT_EURO);
		this.register(Unit.UNIT_USD);
		this.register(Unit.UNIT_JPY);
		this.register(Unit.UNIT_GBP);
		this.register(Unit.UNIT_CHF);
		this.setDefaultUnit(Unit.UNIT_EURO);

		this.register(Unit.UNIT_KILOGRAM);
		this.register(Unit.UNIT_GRAM);
		this.register(Unit.UNIT_MILLIGRAM);
		this.register(Unit.UNIT_MICROGRAM);
		this.register(Unit.UNIT_POUND);
		this.register(Unit.UNIT_STONE);
		this.register(Unit.UNIT_OUNCE);
		this.register(Unit.UNIT_DRAM);
		this.register(Unit.UNIT_GRAIN);
		this.setDefaultUnit(Unit.UNIT_GRAM);

		this.register(Unit.UNIT_KILOCALORIE);
		this.register(Unit.UNIT_KILOJOULE);
		this.setDefaultUnit(Unit.UNIT_KILOCALORIE);

		this.register(Unit.UNIT_CENTILITER);
		this.register(Unit.UNIT_MILLILITER);
		this.register(Unit.UNIT_US_GALLON);
		this.register(Unit.UNIT_US_QUART);
		this.register(Unit.UNIT_US_CUP);
		this.register(Unit.UNIT_US_FLUID_OUNCE);
		this.setDefaultUnit(Unit.UNIT_MILLILITER);

		this.register(Unit.UNIT_CENTIMETER);
		this.register(Unit.UNIT_FOOT);
		this.register(Unit.UNIT_INCH);
		this.setDefaultUnit(Unit.UNIT_CENTIMETER);
	}

	/**
	 * @param unit
	 * @return previously registered unit or {@code null}
	 */
	public Unit register(Unit unit) {
		all = null;
		byType.put(unit.type.ordinal(), null);
		return bySymbol.put(unit.symbol, unit);
	}

	public Unit getUnit(String symbol) {
		return bySymbol.get(symbol);
	}

	public List<Unit> getAllUnits() {
		if (all == null) {
			all = new ArrayList<Unit>();
			for (String symbol : bySymbol.keySet()) {
				all.add(bySymbol.get(symbol));
			}
		}
		return all;
	}

	public List<Unit> getUnitsByType(Unit.Type unitType) {
		if (unitType == null || unitType == Type.UNSPECIFIED) {
			return getAllUnits();
		}
		List<Unit> units = byType.get(unitType.ordinal());
		if (units == null) {
			units = new ArrayList<Unit>();
			for (String symbol : bySymbol.keySet()) {
				Unit unit = bySymbol.get(symbol);
				if (unit.type == unitType) {
					units.add(unit);
				}
			}
			byType.put(unitType.ordinal(), units);
		}
		return units;
	}

	public Unit getDefaultUnitByType(Unit.Type unitType) {
		return defaultByType.get(unitType.ordinal());
	}

	public void setDefaultUnit(Unit unit) {
		defaultByType.put(unit.type.ordinal(), unit);
	}
}
