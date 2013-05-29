package com.primateer.daikoku.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.ShoppingItem;

public class ShoppingItemDao extends Dao<ShoppingItem> {

	public static final String TABLE = "shopping_item";
	public static final String COL_PRODUCT = "product";
	public static final String COL_AMOUNT = "amount";
	public static final String COL_CHECKED = "checked";

	protected ShoppingItemDao() {
	}

	@Override
	protected ShoppingItem buildFrom(Cursor q) {
		Product product = (Product) DBController.getInstance().load(
				Product.class, q.getLong(q.getColumnIndex(COL_PRODUCT)));
		Amount amount = new Amount(q.getString(q.getColumnIndex(COL_AMOUNT)));
		boolean checked = q.getInt(q.getColumnIndex(COL_CHECKED)) != 0;
		return new ShoppingItem(product, amount, checked);
	}

	@Override
	protected ContentValues toCV(ShoppingItem vo) {
		long id = vo.getId();
		ContentValues vals = new ContentValues();
		if (id >= 0) {
			vals.put(COL_ID, id);
		}
		vals.put(COL_PRODUCT, vo.product.getId());
		vals.put(COL_AMOUNT, vo.getAmount().toString());
		vals.put(COL_CHECKED, vo.isChecked());
		return vals;
	}

	@Override
	protected String getTable() {
		return TABLE;
	}
}
