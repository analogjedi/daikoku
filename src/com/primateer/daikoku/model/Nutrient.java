package com.primateer.daikoku.model;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;

public class Nutrient {

	public static abstract class Type extends ValueObject {
		public final String id;
		public final Unit defaultUnit;

		protected Type(String id, Unit.Type unitType) {
			this(id,UnitRegistry.getInstance().getDefaultUnitByType(unitType));
		}
		
		protected Type(String id, Unit defaultUnit) {
			this.id = id;
			this.defaultUnit = defaultUnit;
		}
		
		public Amount getNullAmount() {
			return new Amount(0,defaultUnit);
		}

		@Override
		public String toString() {
			return getName();
		}

		public abstract String getName();
		public abstract String getAbbrev();

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Type other = (Type) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
	}

	private static class BuiltInType extends Type {
		private final int name;

		protected BuiltInType(String id, int name, Unit defaultUnit) {
			super(id, defaultUnit);
			this.name = name;
		}

		@Override
		public String getName() {
			return Application.getContext().getString(name);
		}

		@Override
		public String getAbbrev() {
			// TODO make configurable
			return id;
		}
	}

	public static final Type TYPE_ENERGY = new BuiltInType("E",
			R.string.nutrient_type_energy, Unit.UNIT_KILOCALORIE);
	public static final Type TYPE_PROTEIN = new BuiltInType("P",
			R.string.nutrient_type_protein, Unit.UNIT_GRAM);
	public static final Type TYPE_CARBS = new BuiltInType("C",
			R.string.nutrient_type_carbs, Unit.UNIT_GRAM);
	public static final Type TYPE_FAT = new BuiltInType("F", 
			R.string.nutrient_type_fat, Unit.UNIT_GRAM);
	public static final Type TYPE_SATURATED_FAT = new BuiltInType("Fs",
			R.string.nutrient_type_saturated_fat, Unit.UNIT_GRAM);
	public static final Type TYPE_CHOLESTEROL = new BuiltInType("CH",
			R.string.nutrient_type_cholesterol, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_SODIUM = new BuiltInType("S",
			R.string.nutrient_type_sodium, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_FIBER = new BuiltInType("Cf",
			R.string.nutrient_type_fiber, Unit.UNIT_GRAM);
	public static final Type TYPE_SUGAR = new BuiltInType("Cs",
			R.string.nutrient_type_sugar, Unit.UNIT_GRAM);

	public final Nutrient.Type type;
	public final Amount amount;

	public Nutrient(Nutrient.Type type, Amount amount) {
		this.type = type;
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nutrient other = (Nutrient) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
