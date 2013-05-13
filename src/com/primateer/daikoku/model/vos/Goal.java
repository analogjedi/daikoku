package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutritionHolder;
import com.primateer.daikoku.model.ValueObject;

public class Goal extends ValueObject {

	public enum Type {
		MINIMUM(R.string.abbrev_minimum), MAXIMUM(R.string.abbrev_maximum);

		private String label;

		private Type(int stringId) {
			this.label = Application.getInstance().getResources()
					.getString(stringId);
		}

		public String toString() {
			return label;
		}
	}

	public enum Scope {
		PER_MEAL(R.string.per_meal), PER_DAY(R.string.per_day), PER_WEEK(
				R.string.per_week), PER_MONTH(R.string.per_month);

		private String label;

		private Scope(int stringId) {
			this.label = Application.getInstance().getResources()
					.getString(stringId);
		}

		public String toString() {
			return label;
		}
	}

	public enum Status {
		UNRATED(Application.TEXTCOLOR_GREY), MET(Application.TEXTCOLOR_GREEN), ACHIEVABLE(
				Application.TEXTCOLOR_BLUE), FAILED(Application.TEXTCOLOR_RED);

		public final int color;

		Status(int color) {
			this.color = color;
		}
	}

	public final Type type;
	public final Scope scope;
	public final Nutrient.Type nutrientType;
	public final Amount amount;

	public Goal(Type type, Scope scope, Nutrient.Type nutrientType,
			Amount amount) {
		this.type = type;
		this.scope = scope;
		this.nutrientType = nutrientType;
		this.amount = amount;
	}

	public String toString() {
		return type + " " + amount + " " + nutrientType + " " + scope;
	}

	public Status match(Amount amount) {
		try {
			Amount other = amount.convert(this.amount.unit);
			switch (type) {
			case MINIMUM:
				if (other.value < this.amount.value) {
					return Status.ACHIEVABLE;
				} else {
					return Status.MET;
				}
			case MAXIMUM:
				if (other.value > this.amount.value) {
					return Status.FAILED;
				} else {
					return Status.MET;
				}
			default:
				throw new RuntimeException(
						"Unable to match goal: invalid goal type");
			}
		} catch (UnitConversionException e) {
			Helper.logErrorStackTrace(this, e, "Unable to match goal:");
			return Status.UNRATED;
		}
	}

	public Status match(NutritionHolder nutrition) {
		try {
			return match(nutrition.getNutrition(nutrientType));
		} catch (UnitConversionException e) {
			Helper.logErrorStackTrace(this, e, "Unable to match goal:");
			return Status.UNRATED;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nutrientType == null) ? 0 : nutrientType.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
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
		Goal other = (Goal) obj;
		if (nutrientType == null) {
			if (other.nutrientType != null)
				return false;
		} else if (!nutrientType.equals(other.nutrientType))
			return false;
		if (scope != other.scope)
			return false;
		if (type != other.type)
			return false;
		return true;
	}	
}
