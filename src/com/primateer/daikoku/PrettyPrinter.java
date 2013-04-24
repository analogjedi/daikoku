package com.primateer.daikoku;

import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;

public class PrettyPrinter {

	public static String toString(Nutrient o) {
		return o.type.toString() + ": " + o.amount.toString();
	}

	public static String toString(Nutrition o) {
		StringBuilder str = new StringBuilder();
		str.append("NUTRITION: ");
		str.append("Ref.: ").append(o.getReferenceAmount().toString());
		for (Nutrient nut : o.getNutrients()) {
			str.append("; ").append(toString(nut));
		}
		return str.toString();
	}
	
	public static String toString(Product o) {
		StringBuilder str = new StringBuilder();
		str.append("PRODUCT \"").append(o.getLabel()).append("\": ");
		str.append(o.getAmount());
		str.append(" (").append(o.getUnits()).append(" units); ");
		str.append(toString(o.getNutrition()));
		return str.toString();
	}
}
