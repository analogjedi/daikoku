package com.primateer.daikoku.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Goal.Scope;

public class GoalDao extends Dao<Goal> {

	public static final String TABLE = "goal";
	public static final String COL_TYPE = "goal_type";
	public static final String COL_SCOPE = "scope";
	public static final String COL_AMOUNT = "amount";
	public static final String COL_NUTRIENT_TYPE = "nutrient_type";

	@Override
	public Goal load(long id) {
		Goal vo = null;
		Cursor q = getResolver().query(getUri(TABLE), null, whereId(id), null,
				null);
		if (q.moveToFirst()) {
			vo = buildFrom(q);
		}
		q.close();
		return vo;
	}

	@Override
	public List<Goal> loadAll() {
		return loadAll("");
	}
	
	public List<Goal> loadAll(String where) {
		ArrayList<Goal> results = new ArrayList<Goal>();
		Cursor q = getResolver().query(getUri(TABLE), null, where, null, null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			results.add(buildFrom(q));
		}
		q.close();
		return results;
	}
	
	public List<Goal> loadAll(Scope scope) {
		return loadAll(where(COL_SCOPE,Goal.Scope.PER_DAY.ordinal()));
	}

	@Override
	public long insert(Goal vo) {
		return vo.setId(getId(getResolver().insert(getUri(TABLE), toCV(vo))));
	}

	@Override
	public int update(Goal vo) {
		return getResolver().update(getUri(TABLE), toCV(vo),
				whereId(vo.getId()), null);
	}

	@Override
	public int delete(Goal vo) {
		return delete(TABLE, vo.getId());
	}

	@Override
	protected Goal buildFrom(Cursor q) {
		Goal.Type type = Goal.Type.values()[q
				.getInt(q.getColumnIndex(COL_TYPE))];
		Goal.Scope scope = Goal.Scope.values()[q.getInt(q
				.getColumnIndex(COL_SCOPE))];
		Nutrient.Type nutrientType = NutrientRegistry.getInstance().getType(
				q.getString(q.getColumnIndex(COL_NUTRIENT_TYPE)));
		Amount amount = new Amount(q.getString(q.getColumnIndex(COL_AMOUNT)));
		Goal vo = new Goal(type, scope, nutrientType, amount);
		vo.setId(q.getLong(q.getColumnIndex(COL_ID)));
		return vo;
	}

	@Override
	protected ContentValues toCV(Goal vo) {
		long id = vo.getId();
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(COL_AMOUNT, vo.amount.toString());
		vals.put(COL_NUTRIENT_TYPE,vo.nutrientType.id);
		vals.put(COL_SCOPE,vo.scope.ordinal());
		vals.put(COL_TYPE,vo.type.ordinal());
		return vals;
	}
}
