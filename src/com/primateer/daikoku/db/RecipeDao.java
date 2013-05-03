package com.primateer.daikoku.db;

import java.util.ArrayList;
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
	public static final String RECIPE_COL_FAVORITE = "is_favorite";
	public static final String INGREDIENT_TABLE = "recipe_ingredient";
	public static final String INGREDIENT_COL_RECIPE = "recipe";
	public static final String INGREDIENT_COL_PRODUCT = "product";
	public static final String INGREDIENT_COL_AMOUNT = "amount";

	@Override
	public long insert(Recipe vo) {
		long id = vo.setId(getId(getResolver().insert(getUri(RECIPE_TABLE),
				toCV(vo))));
		for (ContentValues vals : toCV(vo.getIngredients(), id)) {
			getResolver().insert(getUri(INGREDIENT_TABLE), vals);
		}
		return id;
	}

	private List<ContentValues> toCV(Map<Product, Amount> ingredients, long id) {
		List<ContentValues> list = new ArrayList<ContentValues>();
		if (ingredients != null) {
			for (Product product : ingredients.keySet()) {
				ContentValues vals = new ContentValues();
				vals.put(INGREDIENT_COL_RECIPE, id);
				vals.put(INGREDIENT_COL_PRODUCT,
						Data.getInstance().register(product));
				vals.put(INGREDIENT_COL_AMOUNT, ingredients.get(product)
						.toString());
				list.add(vals);
			}
		}
		return list;
	}

	@Override
	public Recipe load(long id) {
		Recipe vo = null;
		Cursor q = getResolver().query(getUri(RECIPE_TABLE), null, whereId(id),
				null, null);
		if (q.moveToFirst()) {
			vo = buildRecipe(q);
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

	private Recipe buildRecipe(Cursor q) {
		Recipe vo = new Recipe();
		long id = q.getLong(q.getColumnIndex(COL_ID));
		vo.setId(id);
		vo.setLabel(q.getString(q.getColumnIndex(COL_LABEL)));
		vo.setFavorite(q.getInt(q.getColumnIndex(RECIPE_COL_FAVORITE)) > 0);
		vo.setIngredients(loadIngredients(id));
		return vo;
	}

	@Override
	public List<Recipe> loadAll() {
		return loadAll(null);
	}

	public List<Recipe> loadAll(String where) {
		ArrayList<Recipe> results = new ArrayList<Recipe>();
		Cursor q = getResolver().query(getUri(RECIPE_TABLE), null, where, null,
				null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			results.add(buildRecipe(q));
		}
		q.close();
		return results;
	}

	public List<Recipe> loadFavorites() {
		return loadAll(RECIPE_COL_FAVORITE + "!= 0");
	}

	@Override
	public int update(Recipe vo) {
		for (ContentValues vals : toCV(vo.getIngredients(), vo.getId())) {
			getResolver().update(
					getUri(INGREDIENT_TABLE),
					vals,
					where(new String[] { INGREDIENT_COL_RECIPE,
							INGREDIENT_COL_PRODUCT }, new Object[] {
							vo.getId(), vals.get(INGREDIENT_COL_PRODUCT) }),
					null);
		}
		return getResolver().update(getUri(RECIPE_TABLE), toCV(vo),
				whereId(vo.getId()), null);
	}

	@Override
	public int delete(Recipe vo) {
		getResolver().delete(getUri(INGREDIENT_TABLE),
				where(INGREDIENT_COL_RECIPE, vo.getId()), null);
		return delete(RECIPE_TABLE, vo.getId());
	}

	@Override
	protected Recipe buildFrom(Cursor q) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ContentValues toCV(Recipe vo) {
		long id = vo.getId();
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(COL_LABEL, vo.getLabel());
		vals.put(RECIPE_COL_FAVORITE, vo.isFavorite());
		return vals;
	}
}
