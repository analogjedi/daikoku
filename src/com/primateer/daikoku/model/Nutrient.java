package com.primateer.daikoku.model;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;

public class Nutrient {

	public static abstract class Type {
		public final String id;
		public final int unitType;

		protected Type(String id, int unitType) {
			this.id = id;
			this.unitType = unitType;
		}

		@Override
		public String toString() {
			return getName();
		}

		public abstract String getName();

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

		protected BuiltInType(String id, int unitType, int name) {
			super(id, unitType);
			this.name = name;
		}

		public String getName() {
			return Application.getContext().getString(name);
		}
	}

	public static final Type TYPE_ENERGY = new BuiltInType("E",
			Unit.TYPE_ENERGY, R.string.nutrient_type_energy);
	public static final Type TYPE_PROTEIN = new BuiltInType("P",
			Unit.TYPE_MASS, R.string.nutrient_type_protein);
	public static final Type TYPE_CARBS = new BuiltInType("C", Unit.TYPE_MASS,
			R.string.nutrient_type_carbs);
	public static final Type TYPE_FAT = new BuiltInType("F", Unit.TYPE_MASS,
			R.string.nutrient_type_fat);

	public final Nutrient.Type type;
	public final Amount amount;

	public Nutrient(Nutrient.Type type, Amount amount) {
		this.type = type;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return type.toString() + ": " + amount.toString();
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
