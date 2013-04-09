package com.primateer.daikoku;

import android.database.Cursor;

public class Application extends android.app.Application {

	private static Application instance = null;

	public static Application getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		System.out.println("Initializing App!");
		initDB();
	}

	private void initDB() {
		System.out.println("Deleting DB!!!!!");
		this.getApplicationContext().deleteDatabase(Database.DB_NAME);
		System.out.println("Initializing DB!");
		Database db = new Database();
		String[] cols = { "label" };
		Cursor q = db.getReadableDatabase().query("vendor", cols, null, null,
				null, null, null, null);
		for (q.moveToFirst(); !q.isAfterLast(); q.moveToNext()) {
			System.out.println(q.getString(q.getColumnIndex("label")));
		}
		q.close();
		db.close();
	}
}
