package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.pojos.Amount;
import com.primateer.daikoku.pojos.Amount.UnitConversionException;

public class Recipe {

	private long id = -1;
	private String label;
	private Map<Product, Amount> ingredients;

	public Recipe add(Product product, Amount amount) {
		getIngredients().put(product, amount);
		return this;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
		Amount total = Amount.NULL;
		if (ingredients != null) {
			for (Product product : ingredients.keySet()) {
				total = total.add(product.getNutrition().get(type,
						ingredients.get(product)));
			}
		}
		return total;
	}
}
