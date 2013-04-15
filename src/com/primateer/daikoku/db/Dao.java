package com.primateer.daikoku.db;

import com.primateer.daikoku.Application;

import android.database.sqlite.SQLiteDatabase;

// TODO a template (<Object>) may help
public abstract class Dao {

	public static final String COL_ID = "_id";
	public static final String COL_LABEL = "label";

	protected SQLiteDatabase getDB() {
		return new DatabaseHelper(Application.getInstance()
				.getApplicationContext()).getWritableDatabase();
	}

	protected static String whereId(long id) {
		return where(COL_ID, id);
	}

	protected static String where(String column, Object value) {
		return column + " = '" + value.toString() + "'";
	}
}
