package com.primateer.daikoku.model.vos;

import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Settings;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.model.Amount.UnitConversionException;

public class Recipe extends ValueObject<Recipe> {

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

	public Amount getTotalNutrition(String type) throws UnitConversionException {
		Amount total = Settings.getInstance().getNullMassAmount();
		if (ingredients != null) {
			for (Product product : ingredients.keySet()) {
				total = total.add(product.getNutrition().getAmount(type,
						ingredients.get(product)));
			}
		}
		return total;
	}
}
