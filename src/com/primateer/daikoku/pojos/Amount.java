package com.primateer.daikoku.pojos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Amount {

	public final double value;
	public final String unit;

	private static final String DOUBLE_REGEXP = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
	private static final Pattern AMOUNT_PATTERN = Pattern.compile("("
			+ DOUBLE_REGEXP + ")([^\\d\\s].*)");

	/**
	 * Parse Amount from compound String.
	 * 
	 * @param amount
	 *            Value followed directly by unit, e.g. "100g" for 100 grams.
	 *            Floating point values are allowed.
	 */
	public Amount(String amount) {
		Matcher m = AMOUNT_PATTERN.matcher(amount.trim());
		if (!m.matches()) {
			throw new IllegalArgumentException("\"" + amount
					+ "\" doesn't match Amount format.");
		}
		this.value = Double.parseDouble(m.group(1));
		this.unit = m.group(3);
	}

	public Amount(double value, String unit) {
		this.value = value;
		this.unit = unit;
	}

	@Override
	public String toString() {
		return this.value + this.unit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	// TODO unit conversion
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Amount other = (Amount) obj;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}
}
