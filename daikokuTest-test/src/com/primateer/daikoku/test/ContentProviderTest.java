package com.primateer.daikoku.test;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.db.DatabaseProvider;

public class ContentProviderTest extends ProviderTestCase2<DatabaseProvider> {

	public ContentProviderTest() {
		super(DatabaseProvider.class, DatabaseProvider.AUTHORITY);
	}

	public void testCP() {
		Uri uri = Uri.parse("content://" + DatabaseProvider.AUTHORITY + "/vendor");
		ContentResolver resolver = Application.getInstance()
				.getContentResolver();

		Cursor q = resolver.query(uri, null, null, null, null);
		q.moveToFirst();
		assertEquals("Aldi", q.getString(q.getColumnIndex("label")));
		q.moveToNext();
		assertEquals("Norma", q.getString(q.getColumnIndex("label")));
		q.moveToNext();
		assertEquals("Netto", q.getString(q.getColumnIndex("label")));
		q.close();
	}
}
