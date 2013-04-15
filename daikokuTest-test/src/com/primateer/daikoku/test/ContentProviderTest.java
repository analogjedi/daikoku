package com.primateer.daikoku.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.primateer.daikoku.db.DatabaseProvider;
import com.primateer.daikoku.testutil.DatabaseTestCase;

public class ContentProviderTest extends DatabaseTestCase {

	ContentResolver resolver;
	Uri uri;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		resolver = this.getMockContentResolver();
		uri = Uri.parse("content://" + DatabaseProvider.AUTHORITY + "/vendor");
	}

	public void testQuery() {
		Cursor q = resolver.query(uri, null, null, null, null);
		assertEquals(3, q.getCount());
		q.moveToFirst();
		assertEquals("Aldi", q.getString(q.getColumnIndex("label")));
		q.moveToNext();
		assertEquals("Norma", q.getString(q.getColumnIndex("label")));
		q.moveToNext();
		assertEquals("Netto", q.getString(q.getColumnIndex("label")));
		q.close();
	}

	public void testInsert() {
		ContentValues vals = new ContentValues();
		vals.put("label", "TestParadies");
		resolver.insert(uri, vals);

		Cursor q = resolver.query(uri, null, "label = 'TestParadies'", null,
				null);
		assertEquals(1, q.getCount());
		q.moveToFirst();
		long id = q.getLong(q.getColumnIndex("_id"));
		q.close();
		
		resolver.insert(uri, vals);
		vals.put("_id", id);
		resolver.insert(uri, vals);
		
		q = resolver.query(uri, null, "label = 'TestParadies'", null,
				null);
		assertEquals(2, q.getCount());
		q.close();
	}

	public void testUpdate() {
		ContentValues vals = new ContentValues();
		vals.put("label","TestParadies");		
		resolver.update(uri, vals, "label = 'Norma'", null);
		
		Cursor q = resolver.query(uri, null, null, null, null);
		assertEquals(3, q.getCount());
		q.moveToFirst();
		assertEquals("Aldi", q.getString(q.getColumnIndex("label")));
		q.moveToNext();
		assertEquals("TestParadies", q.getString(q.getColumnIndex("label")));
		q.moveToNext();
		assertEquals("Netto", q.getString(q.getColumnIndex("label")));
		q.close();
	}
	
	public void testDelete() {
		resolver.delete(uri, "label = 'Norma'", null);
		Cursor q = resolver.query(uri, null, null, null, null);
		assertEquals(2, q.getCount());
	}

}
