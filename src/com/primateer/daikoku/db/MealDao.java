package com.primateer.daikoku.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.model.vos.Meal;

public class MealDao extends Dao {

	public static final String TABLE = "meal";
	public static final String COL_RECIPE = "recipe";
	public static final String COL_DUE = "due";
	public static final String COL_STATE = "state";

	/**
	 * Create new meal entry.
	 * 
	 * @param vo
	 *            value holding object
	 * @return ID of created entry
	 */
	public long insert(Meal vo) {
		ContentValues vals = new ContentValues();
		long oldId = vo.getId();
		if (oldId >= 0) {
			vals.put(COL_ID, oldId);
		}
		vals.put(COL_LABEL, vo.getLabel());
		long recipeId = vo.getRecipe().getId();
		if (recipeId < 0) {
			recipeId = new RecipeDao().insert(vo.getRecipe());
		}
		vals.put(COL_RECIPE, recipeId);
		vals.put(COL_DUE, Helper.toString(vo.getDue()));
		vals.put(COL_STATE, vo.getState());
		long newId = getId(getResolver().insert(getUri(TABLE), vals));
		if (oldId != newId) {
			vo.setId(newId);
		}
		return newId;
	}

	public List<Meal> loadAll(Date date) {
		ArrayList<Meal> results = new ArrayList<Meal>();
		Cursor q = getResolver().query(getUri(TABLE), null,
				where(COL_DUE, Helper.toString(date)), null, null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			results.add(buildMeal(q));
		}
		q.close();
		return results;
	}

	private Meal buildMeal(Cursor q) {
		Meal vo = new Meal();
		vo.setId(q.getLong(q.getColumnIndex(COL_ID)));
		vo.setLabel(q.getString(q.getColumnIndex(COL_LABEL)));
		vo.setRecipe(new RecipeDao().load(q.getLong(q
				.getColumnIndex(COL_RECIPE))));
		vo.setDue(Helper.parseDate(q.getString(q.getColumnIndex(COL_DUE))));
		vo.setState(q.getInt(q.getColumnIndex(COL_STATE)));
		return vo;
	}
}
