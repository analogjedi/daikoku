package com.primateer.daikoku;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	public static final String DB_NAME = "daikoku_db";
	public static final int DB_VERSION = 1;

	public Database() {
		super(Application.getInstance().getApplicationContext(), DB_NAME, null,
				DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("Creating database.");
		try {
			// load instructions
			String batch = Helper.loadAsset("db/create.sql")
					+ Helper.loadAsset("db/populate.sql");
			// spoon feed Android one instruction at a time
			String[] instructions = batch.split(";");
			for (int i = 0; i < instructions.length; i++) {
				String instruction = instructions[i].trim();
				// discard empty instructions
				if (!instruction.equals("")) {
					db.execSQL(instructions[i]);
				}
			}
		} catch (Exception e) {
			Helper.logErrorStackTrace(this, e, "Failed to create database");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// no version upgrades so far
	}

}
