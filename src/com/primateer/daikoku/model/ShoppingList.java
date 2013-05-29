package com.primateer.daikoku.model;

import java.util.HashMap;
import java.util.Map;

import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.model.vos.ShoppingItem;

public class ShoppingList extends ValueObject {

	private HashMap<Product, ShoppingItem> items = new HashMap<Product, ShoppingItem>();

	public ShoppingList() {
		super();
		this.setId(0); // there is only one shopping list
	}

	public void add(ShoppingItem item) {
		items.put(item.product, item);
	}
	
	public void add(Recipe recipe) {
		for (Product product : recipe.getIngredients().keySet()) {
			this.add(product, recipe.getIngredients().get(product));
		}
	}

	public void add(Product product) {
		this.add(product, product.getAmount());
	}

	public void add(Product product, Amount amount) {
		ShoppingItem item = items.get(product);
		if (item == null) {
			item = new ShoppingItem(product);
			items.put(product, item);
		}
		try {
			if (amount.unit.type == Unit.Type.COUNT) {
				amount = product.getDefaultAmount().scale(amount.value);
			}
			item.setAmount(item.getAmount().add(amount));
		} catch (UnitConversionException e) {
			// this should never happen
			throw new RuntimeException(e);
		}
	}

	public void setChecked(Product product, boolean checked) {
		try {
			items.get(product).setChecked(checked);
		} catch (NullPointerException e) {
		}
	}

	public boolean isChecked(Product product) {
		try {
			return items.get(product).isChecked();
		} catch (NullPointerException e) {
			return false;
		}
	}

	public Amount getAmount(Product product) {
		return items.get(product).getAmount();
	}
	
	public Map<Product,ShoppingItem> getItems() {
		return items;
	}
}
