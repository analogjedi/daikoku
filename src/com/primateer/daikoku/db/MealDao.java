package com.primateer.daikoku.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.model.vos.Recipe;

public class MealDao extends Dao<Meal> {

	public static final String TABLE = "meal";
	public static final String COL_DUE = "due";
	public static final String COL_STATE = "state";

	@Override
	public long insert(Meal vo) {
		return vo
				.setId(getId(getResolver().insert(getUri(TABLE), toCV(vo))));
	}

	public List<Meal> loadAll(Date date) {
		ArrayList<Meal> results = new ArrayList<Meal>();
		Cursor q = getResolver().query(getUri(TABLE), null,
				where(COL_DUE, Helper.toString(date)), null, null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			results.add(buildFrom(q));
		}
		q.close();
		return results;
	}

	@Override
	public Meal load(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meal> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Meal vo) {
		return getResolver().update(getUri(TABLE), toCV(vo),
				whereId(vo.getId()), null);
	}

	@Override
	public int delete(Meal vo) {
		if (!vo.isFavorite()) {
			(new RecipeDao()).delete((Recipe) vo);
		}
		return delete(TABLE, vo.getId());
	}

	@Override
	protected Meal buildFrom(Cursor q) {
		Meal vo = new Meal();
		long id = q.getLong(q.getColumnIndex(COL_ID));
		vo.setId(id);
		Recipe recipe = (Recipe) Data.getInstance().get(Recipe.class, id);
		vo.add(recipe);
		vo.setLabel(recipe.getLabel());
		vo.setDue(Helper.parseDate(q.getString(q.getColumnIndex(COL_DUE))));
		vo.setState(Meal.State.values()[q.getInt(q.getColumnIndex(COL_STATE))]);
		return vo;
	}

	@Override
	protected ContentValues toCV(Meal vo) {
		ContentValues vals = new ContentValues();
		vals.put(COL_ID, Data.getInstance().register(vo, Recipe.class));
		vals.put(COL_DUE, Helper.toString(vo.getDue()));
		vals.put(COL_STATE, vo.getState().ordinal());
		return vals;
	}
}
