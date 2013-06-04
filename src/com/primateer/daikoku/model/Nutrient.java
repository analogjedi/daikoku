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
	public static final Type TYPE_OMEGA3 = new BuiltInType("Fo3",
			R.string.nutrient_type_omega3, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_OMEGA6 = new BuiltInType("Fo6",
			R.string.nutrient_type_omega6, Unit.UNIT_GRAM);
	public static final Type TYPE_CHOLESTEROL = new BuiltInType("CH",
			R.string.nutrient_type_cholesterol, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_SODIUM = new BuiltInType("S",
			R.string.nutrient_type_sodium, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_POTASSIUM = new BuiltInType("PO",
			R.string.nutrient_type_potassium, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_FIBER = new BuiltInType("Cf",
			R.string.nutrient_type_fiber, Unit.UNIT_GRAM);
	public static final Type TYPE_SUGAR = new BuiltInType("Cs",
			R.string.nutrient_type_sugar, Unit.UNIT_GRAM);
	public static final Type TYPE_VITAMIN_A = new BuiltInType("VA",
			R.string.nutrient_type_vitamin_a, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_VITAMIN_B1 = new BuiltInType("VB1",
			R.string.nutrient_type_vitamin_b1, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_VITAMIN_B2 = new BuiltInType("VB2",
			R.string.nutrient_type_vitamin_b2, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_VITAMIN_B3 = new BuiltInType("VB3",
			R.string.nutrient_type_vitamin_b3, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_VITAMIN_B5 = new BuiltInType("VB5",
			R.string.nutrient_type_vitamin_b5, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_VITAMIN_B6 = new BuiltInType("VB6",
			R.string.nutrient_type_vitamin_b6, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_VITAMIN_B7 = new BuiltInType("VB7",
			R.string.nutrient_type_vitamin_b7, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_VITAMIN_B9 = new BuiltInType("VB9",
			R.string.nutrient_type_vitamin_b9, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_VITAMIN_B12 = new BuiltInType("VB12",
			R.string.nutrient_type_vitamin_b12, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_VITAMIN_C = new BuiltInType("VC",
			R.string.nutrient_type_vitamin_c, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_VITAMIN_D = new BuiltInType("VD",
			R.string.nutrient_type_vitamin_d, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_VITAMIN_E = new BuiltInType("VE",
			R.string.nutrient_type_vitamin_e, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_VITAMIN_K = new BuiltInType("VK",
			R.string.nutrient_type_vitamin_k, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_CALCIUM = new BuiltInType("CA",
			R.string.nutrient_type_calcium, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_IRON = new BuiltInType("FE",
			R.string.nutrient_type_iron, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_PHOSPHOR = new BuiltInType("PH",
			R.string.nutrient_type_phosphor, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_MAGNESIUM = new BuiltInType("MG",
			R.string.nutrient_type_magnesium, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_ZINC = new BuiltInType("ZN",
			R.string.nutrient_type_zinc, Unit.UNIT_MILLIGRAM);
	public static final Type TYPE_IODINE = new BuiltInType("I",
			R.string.nutrient_type_iodine, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_SELENIUM = new BuiltInType("SE",
			R.string.nutrient_type_selenium, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_COPPER = new BuiltInType("CU",
			R.string.nutrient_type_copper, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_MANGANESE = new BuiltInType("MN",
			R.string.nutrient_type_manganese, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_CHROMIUM = new BuiltInType("CR",
			R.string.nutrient_type_chromium, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_MOLYBDENUM = new BuiltInType("MO",
			R.string.nutrient_type_molybdenum, Unit.UNIT_MICROGRAM);
	public static final Type TYPE_CHLORIDE = new BuiltInType("CL",
			R.string.nutrient_type_chloride, Unit.UNIT_MILLIGRAM);
	

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
