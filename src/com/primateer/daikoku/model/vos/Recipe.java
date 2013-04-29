package com.primateer.daikoku.model.vos;

import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Ingredient;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.ValueObject;

public class Recipe extends ValueObject<Recipe> implements Ingredient {

	private String label;
	private Map<Product, Amount> ingredients;

	public Recipe add(Product product, Amount amount) {
		getIngredients().put(product, amount);
		return this;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<Product, Amount> getIngredients() {
		if (ingredients == null) {
			ingredients = new HashMap<Product, Amount>();
		}
		return ingredients;
	}

	public void setIngredients(Map<Product, Amount> ingredients) {
		this.ingredients = ingredients;
	}

	@Override
	public Amount getNutrition(Nutrient.Type type)
			throws UnitConversionException {
		Amount total = new Amount(0, UnitRegistry.getInstance()
				.getDefaultUnitByType(Unit.Type.MASS));
		if (ingredients != null) {
			for (Ingredient ingredient : ingredients.keySet()) {
				Amount ia = ingredients.get(ingredient);
				if (ia.unit.type == Unit.Type.COUNT) {
					ia = ingredient.getAmountPerUnit().scale(ia.value);
				}
				total = total.add(ingredient.getNutrition(type).scale(
						ia.divideBy(ingredient.getDefaultAmount())));
			}
		}
		return total;
	}

	@Override
	public String toString() {
		if (Helper.isEmpty(label)) {
			return Application.getContext().getResources()
					.getString(R.string.placeholder_empty);
		}
		return label;
	}

	@Override
	public double getUnits() {
		return 1;
	}

	@Override
	public Amount getDefaultAmount() {
		return new Amount(1, Unit.UNIT_UNITS);
	}

	@Override
	public Amount getAmountPerUnit() {
		// TODO Auto-generated method stub
		return null;
	}
}
