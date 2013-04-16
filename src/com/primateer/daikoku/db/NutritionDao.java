package com.primateer.daikoku.db;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.vos.Nutrition;

public class NutritionDao extends Dao {

	public static final String NUTRITION_TABLE = "nutrition";
	public static final String NUTRITION_COL_AMOUNT = "reference_amount";
	public static final String NUTRIENT_TABLE = "nutrient";
	public static final String NUTRIENT_COL_NUTRITION = "nutrition";
	public static final String NUTRIENT_COL_TYPE = "type";
	public static final String NUTRIENT_COL_AMOUNT = "amount";

	public Nutrition load(long id) {
		Nutrition vo = null;
		Cursor q = getResolver().query(getUri(NUTRITION_TABLE), null,
				whereId(id), null, null);
		if (q.moveToFirst()) {
			vo = new Nutrition();
			vo.setId(id);
			vo.setReferenceAmount(new Amount(q.getString(q
					.getColumnIndex(NUTRITION_COL_AMOUNT))));
			vo.setNutrients(loadNutrients(id));
		}
		q.close();
		return vo;
	}

	private Map<String, Amount> loadNutrients(long id) {
		Map<String, Amount> result = new HashMap<String, Amount>();
		Cursor q = getResolver().query(getUri(NUTRIENT_TABLE), null,
				where(NUTRIENT_COL_NUTRITION, id), null, null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			String type = q.getString(q.getColumnIndex(NUTRIENT_COL_TYPE));
			Amount amount = new Amount(q.getString(q
					.getColumnIndex(NUTRIENT_COL_AMOUNT)));
			result.put(type, amount);
		}
		q.close();
		return result;
	}

	public long insert(Nutrition vo) {
		long id = vo.getId();

		// nutrition table
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(NUTRITION_COL_AMOUNT, vo.getReferenceAmount().toString());
		id = getId(getResolver().insert(getUri(NUTRITION_TABLE), vals));
		if (vo.getId() != id) {
			vo.setId(id);
		}

		// nutrient table
		Map<String, Amount> nutrients = vo.getNutrients();
		if (nutrients != null) {
			for (String type : nutrients.keySet()) {
				ContentValues nv = new ContentValues();
				nv.put(NUTRIENT_COL_NUTRITION, id);
				nv.put(NUTRIENT_COL_TYPE, type);
				nv.put(NUTRIENT_COL_AMOUNT, nutrients.get(type).toString());
				getResolver().insert(getUri(NUTRIENT_TABLE), nv);
			}
		}

		return id;
	}

}
