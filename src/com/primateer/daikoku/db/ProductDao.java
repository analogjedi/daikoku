package com.primateer.daikoku.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.vos.Product;

public class ProductDao extends Dao {

	public static final String TABLE = "product";
	public static final String COL_NUTRITION = "nutrition";
	public static final String COL_AMOUNT = "amount";
	public static final String COL_UNITS = "units";

	public Product load(long id) {
		Product vo = null;
		Cursor q = getResolver().query(getUri(TABLE), null, whereId(id), null,
				null);
		if (q.moveToFirst()) {
			vo = new Product();
			vo.setId(id);
			vo.setLabel(q.getString(q.getColumnIndex(COL_LABEL)));
			vo.setNutrition(new NutritionDao().load(q.getLong(q
					.getColumnIndex(COL_NUTRITION))));
			vo.setAmount(new Amount(q.getString(q.getColumnIndex(COL_AMOUNT))));
			vo.setUnits(q.getInt(q.getColumnIndex(COL_UNITS)));
		}
		q.close();
		return vo;
	}

	public long insert(Product vo) {
		long id = vo.getId();

		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(COL_LABEL, vo.getLabel());
		long nutritionId = vo.getNutrition().getId();
		if (nutritionId < 0) {
			nutritionId = new NutritionDao().insert(vo.getNutrition());
		}
		vals.put(COL_NUTRITION, nutritionId);
		vals.put(COL_AMOUNT, vo.getAmount().toString());
		vals.put(COL_UNITS, vo.getUnits());
		id = getId(getResolver().insert(getUri(TABLE), vals));
		if (id != vo.getId()) {
			vo.setId(id);
		}

		return id;
	}
}
