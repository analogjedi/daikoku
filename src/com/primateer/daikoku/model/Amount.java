package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Amount {

	public class UnitConversionException extends Exception {
		public UnitConversionException(String msg) {
			super(msg);
		}
	}

	public final double value;
	public final String unit;


	private static final String DOUBLE_REGEXP = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
	private static final Pattern AMOUNT_PATTERN = Pattern.compile("("
			+ DOUBLE_REGEXP + ")([^\\d\\s].*)");

	private static Map<String, Double> conversionRates;
	static {
		// TODO load from database
		conversionRates = new HashMap<String, Double>();
		addConversionRate("lb", "kg", 0.45359237);
	}

	private static void addConversionRate(String source, String target,
			Double rate) {
		conversionRates.put(source + ">" + target, rate);
		conversionRates.put(target + ">" + source, 1 / rate);
	}

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
	
	/**
	 * Multiply this amount by a scalar factor
	 * 
	 * @param factor any double
	 * @return new {@code Amount}
	 */
	public Amount scale(double factor) {
		return new Amount(this.value*factor, this.unit);
	}

	/**
	 * Convert to different unit of measurement.
	 * 
	 * @param unit
	 *            identifier of the unit to convert to
	 * @return new {@code Amount}
	 * @throws UnitConversionException
	 *             if units are incompatible
	 */
	public Amount convert(String unit) throws UnitConversionException {
		// no change
		if (unit.equals(this.unit)) {
			return this;
		}
		// zero always converts
		if (this.value == 0) {
			return new Amount(0,unit);
		}
		// conversion via rate
		Double rate = conversionRates.get(this.unit + ">" + unit);
		if (rate != null) {
			return new Amount(this.value * rate, unit);
		}
		// conversion impossible
		throw new UnitConversionException("Cannot convert from " + this.unit
				+ " to " + unit);
	}

	/**
	 * Add two amounts together.
	 * 
	 * @param other
	 *            amount to add, {@code null} meaning zero
	 * @return new {@code Amount}
	 * @throws UnitConversionException
	 *             if incompatbile
	 */
	public Amount add(Amount other) throws UnitConversionException {
		// treat null amounts as zero
		if (other == null || other.value == 0) {
			return this;
		}
		// return sum for compatible units
		return new Amount(this.value + other.convert(this.unit).value,
				this.unit);
	}

	/**
	 * Divides this amount by another.
	 * 
	 * @param other
	 *            amount to divide by
	 * @return new {@code Amount}
	 * @throws UnitConversionException
	 *             if incompatible
	 */
	public double divideBy(Amount other) throws UnitConversionException {
		return this.value / other.convert(this.unit).value;
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
