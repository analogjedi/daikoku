package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		this.register(Unit.UNIT_EURO);
		this.register(Unit.UNIT_DOLLAR);
		this.setDefaultUnit(Unit.UNIT_EURO);

		this.register(Unit.UNIT_GRAM);
		this.register(Unit.UNIT_KILOGRAM);
		this.register(Unit.UNIT_POUND);
		this.setDefaultUnit(Unit.UNIT_GRAM);

		this.register(Unit.UNIT_KILOCALORIE);
		this.setDefaultUnit(Unit.UNIT_KILOCALORIE);

		this.register(Unit.UNIT_MILLILITER);
		this.setDefaultUnit(Unit.UNIT_MILLILITER);
	}

	/**
	 * @param unit
	 * @return previously registered unit or {@code null}
	 */
	public Unit register(Unit unit) {
		all = null;
		byType.put(unit.type, null);
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

	public List<Unit> getUnitsByType(int unitType) {
		List<Unit> units = byType.get(unitType);
		if (units == null) {
			units = new ArrayList<Unit>();
			for (String symbol : bySymbol.keySet()) {
				Unit unit = bySymbol.get(symbol);
				if (unit.type == unitType) {
					units.add(unit);
				}
			}
			byType.put(unitType, units);
		}
		return units;
	}

	public Unit getDefaultUnitByType(int unitType) {
		return defaultByType.get(unitType);
	}

	public void setDefaultUnit(Unit unit) {
		defaultByType.put(unit.type, unit);
	}
}
