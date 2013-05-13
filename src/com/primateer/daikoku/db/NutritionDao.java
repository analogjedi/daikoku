package com.primateer.daikoku.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.NutrientSet;
import com.primateer.daikoku.model.vos.Nutrition;

public class NutritionDao extends Dao<Nutrition> {

	public static final String NUTRITION_TABLE = "nutrition";
	public static final String NUTRITION_COL_AMOUNT = "reference_amount";
	public static final String NUTRIENT_TABLE = "nutrient";
	public static final String NUTRIENT_COL_NUTRITION = "nutrition";
	public static final String NUTRIENT_COL_TYPE = "type";
	public static final String NUTRIENT_COL_AMOUNT = "amount";

	protected NutritionDao() {
	}

	private NutrientSet loadNutrients(long id) {
		NutrientSet result = new NutrientSet();
		Cursor q = getResolver().query(getUri(NUTRIENT_TABLE), null,
				where(NUTRIENT_COL_NUTRITION, id), null, null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			String type = q.getString(q.getColumnIndex(NUTRIENT_COL_TYPE));
			Amount amount = new Amount(q.getString(q
					.getColumnIndex(NUTRIENT_COL_AMOUNT)));
			result.add(new Nutrient(NutrientRegistry.getInstance()
					.getType(type), amount));
		}
		q.close();
		return result;
	}

	@Override
	public long insert(Nutrition vo) {
		long id = super.insert(vo);
		for (ContentValues vals : toCV(vo.getNutrients(), id)) {
			getResolver().insert(getUri(NUTRIENT_TABLE), vals);
		}
		return id;
	}

	private List<ContentValues> toCV(NutrientSet nutrients, long id) {
		List<ContentValues> list = new ArrayList<ContentValues>();
		if (nutrients != null) {
			for (Nutrient nutrient : nutrients) {
				ContentValues vals = new ContentValues();
				vals.put(NUTRIENT_COL_NUTRITION, id);
				vals.put(NUTRIENT_COL_TYPE, nutrient.type.id);
				vals.put(NUTRIENT_COL_AMOUNT, nutrient.amount.toString());
				list.add(vals);
			}
		}
		return list;
	}

	@Override
	public int update(Nutrition vo) {
		for (ContentValues vals : toCV(vo.getNutrients(), vo.getId())) {
			getResolver().update(
					getUri(NUTRIENT_TABLE),
					vals,
					where(new String[] { NUTRIENT_COL_TYPE,
							NUTRIENT_COL_NUTRITION },
							new Object[] { vals.get(NUTRIENT_COL_TYPE),
									vo.getId() }), null);
		}
		return super.update(vo);
	}

	@Override
	public int delete(Nutrition vo) {
		getResolver().delete(getUri(NUTRIENT_TABLE),
				where(NUTRIENT_COL_NUTRITION, vo.getId()), null);
		return super.delete(vo);
	}

	@Override
	protected Nutrition buildFrom(Cursor q) {
		Nutrition vo = new Nutrition();
		setKey(q, vo);
		vo.setReferenceAmount(new Amount(q.getString(q
				.getColumnIndex(NUTRITION_COL_AMOUNT))));
		vo.setNutrients(loadNutrients(vo.getId()));
		return vo;
	}

	@Override
	protected ContentValues toCV(Nutrition vo) {
		long id = vo.getId();
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(NUTRITION_COL_AMOUNT, vo.getReferenceAmount().toString());
		return vals;
	}

	@Override
	protected String getTable() {
		return NUTRITION_TABLE;
	}

}
