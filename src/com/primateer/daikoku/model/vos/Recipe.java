package com.primateer.daikoku.model.vos;

import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.AmountException;
import com.primateer.daikoku.model.Amount.UnknownAmountException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientSet;
import com.primateer.daikoku.model.NutritionHolder;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.ValueObject;

public class Recipe extends ValueObject implements NutritionHolder {

	private String label;
	private boolean favorite;
	private Map<Product, Amount> ingredients;
	private NutrientSet extraNutrition;

	public Recipe add(Product product, Amount amount) {
		getIngredients().put(product, amount);
		return this;
	}

	public String getLabel() {
		return label;
	}
	
	public void add(Recipe other) {
		getIngredients().putAll(other.getIngredients());
		getExtraNutrition().absorb(other.getExtraNutrition());
		if (Helper.isEmpty(label)) {
			setLabel(other.getLabel());
		}
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
	
	public void setExtraNutrition(NutrientSet nutrition) {
		this.extraNutrition = nutrition;
	}

	public NutrientSet getExtraNutrition() {
		if (extraNutrition == null) {
			extraNutrition = new NutrientSet();
		}
		return extraNutrition;
	}

	public void setIngredients(Map<Product, Amount> ingredients) {
		this.ingredients = ingredients;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	@Override
	public Amount getNutrition(Nutrient.Type type)
			throws AmountException {
		Amount total = type.getNullAmount();
//		Amount total = getExtraNutrition().get(type).amount;
		if (ingredients != null) {
			for (Product product : ingredients.keySet()) {
				Amount ia = ingredients.get(product);
				if (ia.unit.type == Unit.Type.COUNT) {
					ia = product.getAmountPerUnit().scale(ia.value);
				}
				total = total.add(product.getNutrition(type).scale(
						ia.divideBy(product.getAmount())));
			}
			return total;
		}
		throw new UnknownAmountException("Ingredients missing");
	}

	@Override
	public String toString() {
		if (Helper.isEmpty(label)) {
			return Application.getContext().getResources()
					.getString(R.string.placeholder_empty);
		}
		return label;
	}
}
