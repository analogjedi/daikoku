package com.primateer.daikoku;

import junit.framework.TestCase;

public class HelperTest extends TestCase {

	public void testDateString() {
		String schnaps = "2011-11-11";
		String christmas = "1999-12-24";
		String[] invalid = { "99-01-01", "afl", "1234-1-32", "1584-94-131",
				"19o2-84-13" };

		assertEquals(schnaps, Helper.toString(Helper.parseDate(schnaps)));
		assertEquals(christmas, Helper.toString(Helper.parseDate(christmas)));

		for (int i = 0; i < invalid.length; i++) {
			try {
				Helper.parseDate(invalid[i]);
				fail("Exception expected for invalid input: " + invalid[i]);
			} catch (Exception e) {
			}
		}
	}
}
