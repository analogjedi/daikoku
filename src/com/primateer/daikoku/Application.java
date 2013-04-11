package com.primateer.daikoku;


public class Application extends android.app.Application {

	private static Application instance = null;

	public static Application getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
}
