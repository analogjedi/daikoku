package com.primateer.daikoku.db;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.ValueObject;

public abstract class Dao<T extends ValueObject> {

	public static final String COL_ID = "_id";
	public static final String COL_LABEL = "label";

	private ContentResolver resolver;

	protected ContentResolver getResolver() {
		if (resolver == null) {
			resolver = Application.getContext().getContentResolver();
		}
		return resolver;
	}

	protected static String whereId(long id) {
		return where(COL_ID, id);
	}

	protected static String where(String column, Object value) {
		return column + " = '" + value.toString() + "'";
	}

	protected static String where(String[] columns, Object[] values) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < columns.length; i++) {
			sb.append(where(columns[i], values[i]));
			if (i < columns.length - 1) {
				sb.append(" AND ");
			}
		}
		return sb.toString();
	}

	protected static Uri getUri(String table) {
		return Uri.parse("content://" + DatabaseProvider.AUTHORITY + "/"
				+ table);
	}

	protected static long getId(Uri uri) {
		List<String> segments = uri.getPathSegments();
		return Long.parseLong(segments.get(segments.size() - 1));
	}

	public abstract T load(long id);

	public abstract List<T> loadAll();

	public abstract long insert(T vo);

	public abstract int update(T vo);

	public abstract int delete(T vo);

	protected abstract T buildFrom(Cursor q);

	protected abstract ContentValues toCV(T vo);

	protected int delete(String tableName, long id) {
		if (id < 0) {
			return 0;
		}
		return getResolver().delete(getUri(tableName), whereId(id), null);
	}
}
