package com.primateer.daikoku.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;

public class ProductDao extends Dao<Product> {

	public static final String TABLE = "product";
	public static final String COL_NUTRITION = "nutrition";
	public static final String COL_AMOUNT = "amount";
	public static final String COL_UNITS = "units";

	@Override
	public Product load(long id) {
		Product vo = null;
		Cursor q = getResolver().query(getUri(TABLE), null, whereId(id), null,
				null);
		if (q.moveToFirst()) {
			vo = buildFrom(q);
		}
		q.close();
		return vo;
	}

	@Override
	public long insert(Product vo) {
		return vo.setId(getId(getResolver().insert(getUri(TABLE), toCV(vo))));
	}

	@Override
	public List<Product> loadAll() {
		ArrayList<Product> results = new ArrayList<Product>();
		Cursor q = getResolver().query(getUri(TABLE), null, null, null, null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			results.add(buildFrom(q));
		}
		q.close();
		return results;
	}

	@Override
	public int update(Product vo) {
		return getResolver().update(getUri(TABLE), toCV(vo),
				whereId(vo.getId()), null);
	}

	@Override
	public int delete(Product vo) {
		(new NutritionDao()).delete(vo.getNutrition());
		return delete(TABLE, vo.getId());
	}

	@Override
	protected Product buildFrom(Cursor q) {
		Product vo = new Product();
		vo.setId(q.getLong(q.getColumnIndex(COL_ID)));
		vo.setLabel(q.getString(q.getColumnIndex(COL_LABEL)));
		vo.setNutrition((Nutrition) Data.getInstance().get(Nutrition.class,
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
		vals.put(COL_NUTRITION, Data.getInstance().register(vo.getNutrition()));
		vals.put(COL_AMOUNT, vo.getAmount().toString());
		vals.put(COL_UNITS, vo.getUnits());
		return vals;
	}
}
