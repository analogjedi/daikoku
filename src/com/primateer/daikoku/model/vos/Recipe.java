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

	public Amount getTotalNutrition(Nutrient.Type type)
			throws UnitConversionException {
		Amount total = new Amount(0, UnitRegistry.getInstance()
				.getDefaultUnitByType(Unit.TYPE_MASS));
		if (ingredients != null) {
			for (Product product : ingredients.keySet()) {
				total = total.add(product.getNutrition().getAmount(type,
						ingredients.get(product)));
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Amount getDefaultAmount() {
		return new Amount(1,Unit.UNIT_UNITS);
	}
}
