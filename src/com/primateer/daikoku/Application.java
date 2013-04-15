package com.primateer.daikoku;

import android.content.Context;


public class Application extends android.app.Application {

	private static Application instance;
	private static Context alternateContext;

	public static Application getInstance() {
		return instance;
	}
	
	public static Context getContext() {
		if (alternateContext == null) {
			return getInstance().getApplicationContext();
		}
		return alternateContext;
	}
	
	public static void setAlternateContext(Context context) {
		alternateContext = context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
}
