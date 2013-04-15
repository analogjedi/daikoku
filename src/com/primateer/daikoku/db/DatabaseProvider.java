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
			dbHelper = new DatabaseHelper(getContext());
		}
		if (writable) {
			return dbHelper.getWritableDatabase();
		} else {
			return dbHelper.getReadableDatabase();
		}
	}

	private String getTable(Uri uri) {
		return uri.getPathSegments().get(0);
	}

	@Override
	public boolean onCreate() {
		// do nothing
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return getDB(false).query(getTable(uri), projection, selection,
				selectionArgs, null, null, sortOrder);
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id = getDB(true).insert(getTable(uri), null, values);
		return Uri.withAppendedPath(uri, String.valueOf(id));
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return getDB(true).delete(getTable(uri), selection, selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return getDB(true).update(getTable(uri), values, selection,
				selectionArgs);
	}

}
