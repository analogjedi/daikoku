package com.primateer.daikoku.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Supply;

public class SupplyDao extends Dao<Supply> {

	public static final String TABLE = "supply";
	public static final String COL_PRODUCT = "product";
	public static final String COL_UNIT = "unit";
	public static final String COL_AVAILABLE = "available";
	public static final String COL_RESERVED = "reserved";
	public static final String COL_CONSUMED = "consumed";
	public static final String COL_COST = "cost";

	protected SupplyDao() {
	}

	@Override
	protected Supply buildFrom(Cursor q) {
		Product product = (Product) DBController.getInstance().load(
				Product.class, q.getLong(q.getColumnIndex(COL_PRODUCT)));
		Unit unit = UnitRegistry.getInstance().getUnit(
				q.getString(q.getColumnIndex(COL_UNIT)));
		double available = q.getDouble(q.getColumnIndex(COL_AVAILABLE));
		double reserved = q.getDouble(q.getColumnIndex(COL_RESERVED));
		double consumed = q.getDouble(q.getColumnIndex(COL_CONSUMED));
		double total = available + reserved + consumed;
		Amount cost = new Amount(q.getString(q.getColumnIndex(COL_COST)));
		return new Supply(product, unit, total, available, reserved, consumed,
				cost);
	}

	@Override
	protected ContentValues toCV(Supply vo) {
		long id = vo.getId();
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(COL_PRODUCT, vo.product.getId());
		vals.put(COL_UNIT, vo.getTotal().unit.symbol);
		vals.put(COL_AVAILABLE, vo.getAvailable().value);
		vals.put(COL_RESERVED, vo.getReserved().value);
		vals.put(COL_CONSUMED, vo.getConsumed().value);
		vals.put(COL_COST, vo.getCost().toString());
		return vals;
	}

	@Override
	protected String getTable() {
		return TABLE;
	}
}
