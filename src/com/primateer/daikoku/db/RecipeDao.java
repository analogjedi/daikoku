package com.primateer.daikoku.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;

public class RecipeDao extends Dao<Recipe> {

	public static final String RECIPE_TABLE = "recipe";
	public static final String INGREDIENT_TABLE = "recipe_ingredient";
	public static final String INGREDIENT_COL_RECIPE = "recipe";
	public static final String INGREDIENT_COL_PRODUCT = "product";
	public static final String INGREDIENT_COL_AMOUNT = "amount";

	public long insert(Recipe vo) {
		long id = vo.getId();

		// recipe table
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(COL_LABEL, vo.getLabel());
		id = getId(getResolver().insert(getUri(RECIPE_TABLE), vals));
		if (vo.getId() != id) {
			vo.setId(id);
		}

		// recipe_ingredient table
		Map<Product, Amount> ingredients = vo.getIngredients();
		if (ingredients != null) {
			for (Product product : ingredients.keySet()) {
				ContentValues iv = new ContentValues();
				iv.put(INGREDIENT_COL_RECIPE, id);
				iv.put(INGREDIENT_COL_PRODUCT,
						Data.getInstance().register(product));
				iv.put(INGREDIENT_COL_AMOUNT, ingredients.get(product)
						.toString());
				getResolver().insert(getUri(INGREDIENT_TABLE), iv);
			}
		}

		return id;
	}

	public Recipe load(long id) {
		Recipe vo = null;
		Cursor q = getResolver().query(getUri(RECIPE_TABLE), null, whereId(id),
				null, null);
		if (q.moveToFirst()) {
			vo = new Recipe();
			vo.setId(id);
			vo.setLabel(q.getString(q.getColumnIndex(COL_LABEL)));
			vo.setIngredients(loadIngredients(id));
		}
		q.close();
		return vo;
	}

	private Map<Product, Amount> loadIngredients(long id) {
		Map<Product, Amount> ingredients = new HashMap<Product, Amount>();
		Cursor q = getResolver().query(getUri(INGREDIENT_TABLE), null,
				where(INGREDIENT_COL_RECIPE, id), null, null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			Product product = (Product) Data.getInstance().get(Product.class,
					q.getLong(q.getColumnIndex(INGREDIENT_COL_PRODUCT)));
			Amount amount = new Amount(q.getString(q
					.getColumnIndex(INGREDIENT_COL_AMOUNT)));
			ingredients.put(product, amount);
		}
		q.close();
		return ingredients;
	}

	@Override
	public List<Recipe> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Recipe vo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Recipe vo) {
		// TODO Auto-generated method stub
		return 0;
	}
}
