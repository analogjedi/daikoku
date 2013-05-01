package com.primateer.daikoku.db;

import java.util.List;

import android.content.ContentResolver;
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

	protected static Uri getUri(String table) {
		return Uri.parse("content://" + DatabaseProvider.AUTHORITY + "/"
				+ table);
	}
	
	protected static long getId(Uri uri) {
		List<String> segments = uri.getPathSegments();
		return Long.parseLong(segments.get(segments.size()-1));
	}
	
	public abstract T load(long id);
	public abstract List<T> loadAll();
	public abstract long insert(T vo);
	public abstract int update(T vo);
	public abstract int delete(T vo);
}
