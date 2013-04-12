package com.primateer.daikoku.test;

import com.primateer.daikoku.db.DatabaseHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class DatabaseTest extends AndroidTestCase {

	public void testDB() {
		SQLiteDatabase db = new DatabaseHelper().getReadableDatabase();
		String[] cols = { "label" };
		Cursor q = db.query("vendor", cols, null, null,
				null, null, null, null);

		q.moveToFirst();
		assertEquals("Aldi",q.getString(q.getColumnIndex("label")));
		q.moveToNext();
		assertEquals("Norma",q.getString(q.getColumnIndex("label")));
		q.moveToNext();
		assertEquals("Netto",q.getString(q.getColumnIndex("label")));
		
		q.close();
		db.close();
	}
}
