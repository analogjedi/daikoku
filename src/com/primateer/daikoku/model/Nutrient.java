package com.primateer.daikoku.model;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;

public class Nutrient {

	public static abstract class Type {
		public final String id;

		protected Type(String id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			return getName();
		}

		public abstract String getName();
	}

	private static class BuiltInType extends Type {
		private final int name;

		protected BuiltInType(String id, int name) {
			super(id);
			this.name = name;
		}

		public String getName() {
			return Application.getContext().getString(name);
		}
	}

	public static final Type Energy = new BuiltInType("E",
			R.string.nutrient_type_energy);
	public static final Type Protein = new BuiltInType("P",
			R.string.nutrient_type_protein);
	public static final Type Carbs = new BuiltInType("C",
			R.string.nutrient_type_carbs);
	public static final Type Fat = new BuiltInType("F",
			R.string.nutrient_type_fat);

	public final Nutrient.Type type;
	public final Amount amount;

	public Nutrient(Nutrient.Type type, Amount amount) {
		this.type = type;
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return type.toString();
	}
}
