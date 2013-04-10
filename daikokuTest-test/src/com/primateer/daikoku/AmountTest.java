package com.primateer.daikoku;

import com.primateer.daikoku.pojos.Amount;

import junit.framework.TestCase;

public class AmountTest extends TestCase {

	public void testParse() {
		Amount energy = new Amount("366.5kcal");
		assertEquals("kcal",energy.unit);
		assertEquals(366.5,energy.value);
		
		Amount kilometers = new Amount(" \n  40km ");
		assertEquals("km",kilometers.unit);
		assertEquals(40.0,kilometers.value);
		
		String invalid[] = {"inv8l1d", "493", "m123", "10 m"};
		for (int i=0; i<invalid.length; i++)
		try {
			new Amount(invalid[i]);
			fail("Parsing " + invalid[i] + " should throw an Exception.");
		} catch (Exception e) {}
	}
	
	public void testConvert() {
		Amount onepound = new Amount("1lb");

		assertEquals(1.0,onepound.convert("lb").value);
		assertEquals("lb",onepound.convert("lb").unit);
		
		assertNull(onepound.convert("km"));
		
		assertEquals(0.45359237,onepound.convert("kg").value);
		assertEquals("kg",onepound.convert("kg").unit);
	}
}
