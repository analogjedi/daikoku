package com.primateer.daikoku.db;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Recipe;

public class MealDao extends Dao<Meal> {

	public static final String TABLE = "meal";
	public static final String COL_DUE = "due";
	public static final String COL_STATE = "state";

	protected MealDao() {
	}

	public List<Meal> loadAll(Date date) {
		return loadAll(where(COL_DUE, Helper.toString(date)));
	}

	@Override
	public int delete(Meal vo) {
		if (!vo.isFavorite()) {
			(new RecipeDao()).delete((Recipe) vo);
		}
		return super.delete(vo);
	}

	@Override
	protected Meal buildFrom(Cursor q) {
		Meal vo = new Meal();
		setKey(q, vo);
		Recipe recipe = (Recipe) DBController.getInstance().load(Recipe.class,
				vo.getId());
		vo.add(recipe);
		vo.setLabel(recipe.getLabel());
		vo.setDue(Helper.parseDate(q.getString(q.getColumnIndex(COL_DUE))));
		vo.setState(Meal.State.values()[q.getInt(q.getColumnIndex(COL_STATE))]);
		return vo;
	}

	@Override
	protected ContentValues toCV(Meal vo) {
		ContentValues vals = new ContentValues();
		vals.put(COL_ID, DBController.getInstance().register(vo, Recipe.class));
		vals.put(COL_DUE, Helper.toString(vo.getDue()));
		vals.put(COL_STATE, vo.getState().ordinal());
		return vals;
	}

	@Override
	protected String getTable() {
		return TABLE;
	}
}
