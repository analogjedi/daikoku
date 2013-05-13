package com.primateer.daikoku.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;

public class ProductDao extends Dao<Product> {

	public static final String TABLE = "product";
	public static final String COL_NUTRITION = "nutrition";
	public static final String COL_AMOUNT = "amount";
	public static final String COL_UNITS = "units";

	protected ProductDao() {
	}

	@Override
	public int delete(Product vo) {
		(new NutritionDao()).delete(vo.getNutrition());
		return super.delete(vo);
	}

	@Override
	protected Product buildFrom(Cursor q) {
		Product vo = new Product();
		vo.setId(q.getLong(q.getColumnIndex(COL_ID)));
		vo.setLabel(q.getString(q.getColumnIndex(COL_LABEL)));
		vo.setNutrition((Nutrition) Database.getInstance().load(Nutrition.class,
				q.getLong(q.getColumnIndex(COL_NUTRITION))));
		vo.setAmount(new Amount(q.getString(q.getColumnIndex(COL_AMOUNT))));
		vo.setUnits(q.getDouble(q.getColumnIndex(COL_UNITS)));
		return vo;
	}

	@Override
	protected ContentValues toCV(Product vo) {
		long id = vo.getId();
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(COL_LABEL, vo.getLabel());
		vals.put(COL_NUTRITION, Database.getInstance().register(vo.getNutrition()));
		vals.put(COL_AMOUNT, vo.getAmount().toString());
		vals.put(COL_UNITS, vo.getUnits());
		return vals;
	}

	@Override
	protected String getTable() {
		return TABLE;
	}
}
