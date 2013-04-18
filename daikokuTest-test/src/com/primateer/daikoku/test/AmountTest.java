package com.primateer.daikoku.test;

import junit.framework.TestCase;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Unit;

public class AmountTest extends TestCase {

	public void testParse() {
		Amount energy = new Amount("366.5kcal");
		assertEquals(Unit.UNIT_KILOCALORIE, energy.unit);
		assertEquals(366.5, energy.value);

		Amount milliliter = new Amount(" \n  40ml ");
		assertEquals(Unit.UNIT_MILLILITER, milliliter.unit);
		assertEquals(40.0, milliliter.value);

		String invalid[] = { "inv8l1d", "493", "m123", "10 m" };
		for (int i = 0; i < invalid.length; i++)
			try {
				new Amount(invalid[i]);
				fail("Parsing " + invalid[i] + " should throw an Exception.");
			} catch (Exception e) {
			}
	}

	public void testConvert() throws UnitConversionException {
		Amount onepound = new Amount("1lb");

		assertEquals(1.0, onepound.convert(Unit.UNIT_POUND).value);
		assertEquals(Unit.UNIT_POUND, onepound.convert(Unit.UNIT_POUND).unit);

		try {
			onepound.convert(Unit.UNIT_KILOCALORIE);
			fail("Converting " + onepound
					+ " to kcal should throw an Exception.");
		} catch (UnitConversionException e) {
		}

		assertEquals(0.45359237, onepound.convert(Unit.UNIT_KILOGRAM).value);
		assertEquals(Unit.UNIT_KILOGRAM,
				onepound.convert(Unit.UNIT_KILOGRAM).unit);
	}

	public void testAdd() throws UnitConversionException {
		Amount onepound = new Amount("1lb");
		Amount onekilo = new Amount("1kg");

		assertEquals(new Amount("3lb"), onepound.add(onepound).add(onepound));

		assertEquals(new Amount("3.36077711kg"),
				onekilo.add(onepound).add(onepound).add(onepound).add(onekilo));

		try {
			onekilo.add(new Amount("30kcal"));
			fail("Adding 30kcal to " + onekilo + " should throw an Exception.");
		} catch (UnitConversionException e) {
		}
	}
}
