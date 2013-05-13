package com.primateer.daikoku.db;

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

	protected GoalDao() {
	}

	@Override
	public Goal load(long id) {
		throw new IllegalAccessError("Goal record may not be loaded by ID");
	}

	public List<Goal> loadAll(Scope scope) {
		return loadAll(where(COL_SCOPE, Goal.Scope.PER_DAY.ordinal()));
	}

	@Override
	protected String whereKey(Goal vo) {
		return where(new String[] { COL_TYPE, COL_SCOPE, COL_NUTRIENT_TYPE },
				new String[] { "" + vo.type.ordinal(), "" + vo.scope.ordinal(),
						vo.nutrientType.id });
	}

	@Override
	protected void setKey(Cursor q, Goal vo) {
		// do nothing
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
		return new Goal(type, scope, nutrientType, amount);
	}

	@Override
	protected ContentValues toCV(Goal vo) {
		long id = vo.getId();
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(COL_AMOUNT, vo.amount.toString());
		vals.put(COL_NUTRIENT_TYPE, vo.nutrientType.id);
		vals.put(COL_SCOPE, vo.scope.ordinal());
		vals.put(COL_TYPE, vo.type.ordinal());
		return vals;
	}

	@Override
	protected String getTable() {
		return TABLE;
	}
}
