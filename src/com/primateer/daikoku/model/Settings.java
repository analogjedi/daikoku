package com.primateer.daikoku.model;

public class Settings {

	
	private static Settings instance;
	
	public synchronized static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}


	private Amount defaultReferenceAmount = new Amount("100g");
	private String defaultMassUnit = "g";
	private String defaultEnergyUint = "kcal";
	private Amount nullMassAmount;
	
	private Settings() {
		nullMassAmount = new Amount(0,getDefaultMassUnit());
	}

	
	public String getDefaultMassUnit() {
		return defaultMassUnit;
	}
	
	public String getDefaultEnergyUnit() {
		return defaultEnergyUint;
	}
	
	public Amount getDefaultReferenceAmount() {
		return defaultReferenceAmount;
	}
	
	public Amount getNullMassAmount() {
		return nullMassAmount;
	}
}
