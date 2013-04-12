package com.primateer.daikoku.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

// TODO thread safety
public class DatabaseProvider extends ContentProvider {

	public static final String AUTHORITY = "daikoku";

	private SQLiteOpenHelper dbHelper;

	private SQLiteDatabase getDB(boolean writable) {
		if (dbHelper == null) {
			dbHelper = new DatabaseHelper();
		}
		if (writable) {
			return dbHelper.getWritableDatabase();
		} else {
			return dbHelper.getReadableDatabase();
		}
	}

	@Override
	public boolean onCreate() {
		// do nothing
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String table = uri.getPathSegments().get(0);
		return getDB(false).query(table, projection,
				selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
