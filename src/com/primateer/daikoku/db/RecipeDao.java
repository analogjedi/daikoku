package com.primateer.daikoku.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.primateer.daikoku.model.Product;
import com.primateer.daikoku.model.Recipe;
import com.primateer.daikoku.pojos.Amount;

public class RecipeDao extends Dao {

	public static final String RECIPE_TABLE = "recipe";
	public static final String INGREDIENT_TABLE = "recipe_ingredient";
	public static final String INGREDIENT_COL_RECIPE = "recipe";
	public static final String INGREDIENT_COL_PRODUCT = "product";
	public static final String INGREDIENT_COL_AMOUNT = "amount";

	public long insert(Recipe vo) {
		long id = vo.getId();
		SQLiteDatabase db = getDB();

		// recipe table
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(COL_LABEL, vo.getLabel());
		id = db.insertWithOnConflict(RECIPE_TABLE, null, vals,
				SQLiteDatabase.CONFLICT_REPLACE);
		if (vo.getId() != id) {
			vo.setId(id);
		}

		// recipe_ingredient table
		Map<Product, Amount> ingredients = vo.getIngredients();
		if (ingredients != null) {
			for (Product product : ingredients.keySet()) {
				ContentValues iv = new ContentValues();
				iv.put(INGREDIENT_COL_RECIPE, id);
				long productId = product.getId();
				if (productId < 0) {
					productId = new ProductDao().insert(product);
				}
				iv.put(INGREDIENT_COL_PRODUCT, productId);
				iv.put(INGREDIENT_COL_AMOUNT, ingredients.get(product)
						.toString());
				db.insertWithOnConflict(INGREDIENT_TABLE, null, iv,
						SQLiteDatabase.CONFLICT_REPLACE);
			}
		}

		db.close();
		return id;
	}

	public Recipe load(long id) {
		SQLiteDatabase db = getDB();
		Recipe vo = null;
		Cursor q = db.query(RECIPE_TABLE, null, whereId(id), null, null, null,
				null, null);
		if (q.moveToFirst()) {
			vo = new Recipe();
			vo.setId(id);
			vo.setLabel(q.getString(q.getColumnIndex(COL_LABEL)));
			vo.setIngredients(loadIngredients(id));
		}
		q.close();
		db.close();
		return vo;
	}

	private Map<Product, Amount> loadIngredients(long id) {
		SQLiteDatabase db = getDB();
		Map<Product, Amount> ingredients = new HashMap<Product, Amount>();
		Cursor q = db.query(INGREDIENT_TABLE, null,
				where(INGREDIENT_COL_RECIPE, id), null, null, null, null, null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			Product product = new ProductDao().load(q.getLong(q
					.getColumnIndex(INGREDIENT_COL_PRODUCT)));
			Amount amount = new Amount(q.getString(q
					.getColumnIndex(INGREDIENT_COL_AMOUNT)));
			ingredients.put(product, amount);
		}
		q.close();
		db.close();
		return ingredients;
	}
}
