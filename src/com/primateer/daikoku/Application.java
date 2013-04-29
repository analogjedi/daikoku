package com.primateer.daikoku;

import android.content.Context;

public class Application extends android.app.Application {

	public static final int ICON_ADD = R.drawable.onebit_1_31;
	public static final int ICON_REMOVE = R.drawable.onebit_1_32;
	public static final int ICON_CREATE = R.drawable.onebit_1_31;	
	public static final int ICON_DELETE = R.drawable.onebit_1_33;
	public static final int ICON_ACCEPT = R.drawable.onebit_1_34;
	public static final int ICON_DENY = R.drawable.onebit_1_33;
	public static final int ICON_SEARCH = R.drawable.onebit_1_02;
	public static final int ICON_EDIT = R.drawable.onebit_1_20;
	public static final int ICON_STATE_SCHEDULED = R.drawable.onebit_2_01;
	public static final int ICON_STATE_RESERVED = R.drawable.onebit_2_02;
	public static final int ICON_STATE_PREPARED = R.drawable.onebit_2_03;
	public static final int ICON_STATE_CONSUMED = R.drawable.onebit_2_04;
	
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
