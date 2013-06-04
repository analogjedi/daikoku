package com.primateer.daikoku.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.primateer.daikoku.Helper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "daikoku_db";
	public static final int DB_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		loadSQL(db, "db/create.sql");
		// loadSQL("db/populate.sql");
	}

	public void loadSQL(SQLiteDatabase db, String file) {
		try {
			// spoon feed Android one instruction at a time
			String[] instructions = Helper.loadAsset(file).split(";");
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
