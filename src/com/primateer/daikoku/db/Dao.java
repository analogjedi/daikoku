package com.primateer.daikoku.db;

import java.util.ArrayList;
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

	private static String whereId(long id) {
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

	public T load(long id) {
		T vo = null;
		Cursor q = getResolver().query(getUri(getTable()), null, whereId(id),
				null, null);
		if (q.moveToFirst()) {
			vo = buildFrom(q);
		}
		q.close();
		return vo;
	}

	public List<T> loadAll() {
		return loadAll(null);
	}

	public List<T> loadAll(String where) {
		ArrayList<T> results = new ArrayList<T>();
		Cursor q = getResolver().query(getUri(getTable()), null, where, null,
				null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			results.add(buildFrom(q));
		}
		q.close();
		return results;
	}

	public long insert(T vo) {
		return vo.setId(getId(getResolver()
				.insert(getUri(getTable()), toCV(vo))));
	}

	public int update(T vo) {
		return getResolver().update(getUri(getTable()), toCV(vo), whereKey(vo),
				null);
	}

	protected abstract String getTable();

	public int delete(T vo) {
		return getResolver().delete(getUri(getTable()), whereKey(vo), null);
	}

	protected abstract T buildFrom(Cursor q);

	protected abstract ContentValues toCV(T vo);

	protected String whereKey(T vo) {
		return whereId(vo.getId());
	}

	protected void setKey(Cursor q, T vo) {
		long id = q.getLong(q.getColumnIndex(COL_ID));
		vo.setId(id);
	}

}
