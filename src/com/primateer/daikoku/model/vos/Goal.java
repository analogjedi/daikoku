package com.primateer.daikoku.model.vos;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutritionHolder;
import com.primateer.daikoku.model.ValueObject;

public class Goal extends ValueObject {

	public enum Type {
		MINIMUM, MAXIMUM
	}

	public enum Scope {
		PER_MEAL, PER_DAY, PER_WEEK, PER_MONTH;
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
}
