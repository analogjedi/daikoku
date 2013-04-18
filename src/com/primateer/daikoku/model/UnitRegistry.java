package com.primateer.daikoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitRegistry {

	private static UnitRegistry instance;

	public static synchronized UnitRegistry getInstance() {
		if (instance == null) {
			instance = new UnitRegistry();
		}
		return instance;
	}

	private Map<String, Unit> bySymbol = new HashMap<String, Unit>();
	private List<Unit> byType;
	private List<Unit> all;

	private UnitRegistry() {
		this.register(Unit.UNIT_EURO);
		this.register(Unit.UNIT_DOLLAR);
		this.register(Unit.UNIT_GRAM);
		this.register(Unit.UNIT_KILOCALORIE);
		this.register(Unit.UNIT_KILOGRAM);
		this.register(Unit.UNIT_MILLILITER);
		this.register(Unit.UNIT_POUND);
	}

	/**
	 * @param unit
	 * @return previously registered unit or {@code null}
	 */
	public Unit register(Unit unit) {
		all = null;
		byType = null;
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
		if (byType == null) {
			byType = new ArrayList<Unit>();
			for (String symbol : bySymbol.keySet()) {
				Unit unit = bySymbol.get(symbol);
				if (unit.type == unitType) {
					byType.add(unit);
				}
			}
		}
		return byType;
	}
}
