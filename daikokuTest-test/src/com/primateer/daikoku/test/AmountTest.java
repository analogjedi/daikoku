package com.primateer.daikoku.test;

import com.primateer.daikoku.pojos.Amount;
import com.primateer.daikoku.pojos.Amount.UnitConversionException;

import junit.framework.TestCase;

public class AmountTest extends TestCase {

	public void testParse() {
		Amount energy = new Amount("366.5kcal");
		assertEquals("kcal", energy.unit);
		assertEquals(366.5, energy.value);

		Amount kilometers = new Amount(" \n  40km ");
		assertEquals("km", kilometers.unit);
		assertEquals(40.0, kilometers.value);

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

		assertEquals(1.0, onepound.convert("lb").value);
		assertEquals("lb", onepound.convert("lb").unit);

		try {
			onepound.convert("km");
			fail("Converting " + onepound + " to km should throw an Exception.");
		} catch (UnitConversionException e) {
		}

		assertEquals(0.45359237, onepound.convert("kg").value);
		assertEquals("kg", onepound.convert("kg").unit);
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
