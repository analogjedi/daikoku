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
		vals.put(COL_RECIPE, Data.getInstance().register(vo.getRecipe()));
		vals.put(COL_DUE, Helper.toString(vo.getDue()));
		vals.put(COL_STATE, vo.getState().ordinal());
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
		vo.setRecipe((Recipe) Data.getInstance().get(Recipe.class,
				q.getLong(q.getColumnIndex(COL_RECIPE))));
		vo.setDue(Helper.parseDate(q.getString(q.getColumnIndex(COL_DUE))));
		vo.setState(Meal.State.values()[q.getInt(q.getColumnIndex(COL_STATE))]);
		return vo;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Meal vo) {
		// TODO Auto-generated method stub
		return 0;
	}
}
