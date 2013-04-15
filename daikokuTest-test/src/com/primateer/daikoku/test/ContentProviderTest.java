package com.primateer.daikoku.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.primateer.daikoku.db.DatabaseProvider;

public class ContentProviderTest extends ProviderTestCase2<DatabaseProvider> {

	ContentResolver resolver;

	public ContentProviderTest() {
		super(DatabaseProvider.class, DatabaseProvider.AUTHORITY);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		resolver = this.getMockContentResolver();
	}

	public void testQueryCP() {
		Uri uri = Uri.parse("content://" + DatabaseProvider.AUTHORITY
				+ "/vendor");

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

	public void testInsertCP() {
		Uri uri = Uri.parse("content://" + DatabaseProvider.AUTHORITY
				+ "/vendor");

		ContentValues vals = new ContentValues();
		vals.put("label", "TestParadies");
		resolver.insert(uri, vals);

		Cursor q = resolver.query(uri, null, "label = 'TestParadies'", null,
				null);
		assertEquals(1, q.getCount());
		q.close();
	}
}
