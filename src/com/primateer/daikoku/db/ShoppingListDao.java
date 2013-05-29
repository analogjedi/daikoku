package com.primateer.daikoku.db;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.primateer.daikoku.model.ShoppingList;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.ShoppingItem;

/**
 * Only one ShoppingList is supported, so ID is ignored.
 * 
 */
public class ShoppingListDao extends Dao<ShoppingList> {

	public static final String TABLE = "supply";
	public static final String COL_PRODUCT = "product";
	public static final String COL_UNIT = "unit";
	public static final String COL_AVAILABLE = "available";
	public static final String COL_RESERVED = "reserved";
	public static final String COL_CONSUMED = "consumed";
	public static final String COL_COST = "cost";

	protected ShoppingListDao() {
	}

	@Override
	public int deleteAll(String where) {
		return delete(null);
	}

	@Override
	public int delete(ShoppingList vo) {
		ShoppingItemDao dao = new ShoppingItemDao();
		return dao.deleteAll();
	}

	@Override
	public ShoppingList load(long id) {
		ShoppingList vo = new ShoppingList();
		ShoppingItemDao dao = new ShoppingItemDao();
		List<ShoppingItem> items = dao.loadAll();
		for (ShoppingItem item : items) {
			vo.add(item);
		}
		return vo;
	}

	@Override
	public long insert(ShoppingList vo) {
		ShoppingItemDao dao = new ShoppingItemDao();
		Map<Product, ShoppingItem> items = vo.getItems();
		for (Product product : items.keySet()) {
			dao.insert(items.get(product));
		}
		return 0;
	}

	@Override
	public int update(ShoppingList vo) {
		this.delete(vo);
		this.insert(vo);
		return vo.getItems().size();
	}

	@Override
	protected ShoppingList buildFrom(Cursor q) {
		// should never be called
		throw new RuntimeException("ShoppingListDao doesn't have a table.");
	}

	@Override
	protected ContentValues toCV(ShoppingList vo) {
		// should never be called
		throw new RuntimeException("ShoppingListDao doesn't have a table.");
	}

	@Override
	protected String getTable() {
		// should never be called
		throw new RuntimeException("ShoppingListDao doesn't have a table.");
	}
}
