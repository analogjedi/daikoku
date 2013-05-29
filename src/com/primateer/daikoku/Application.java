package com.primateer.daikoku;

import android.content.Context;
import android.graphics.Color;

import com.primateer.daikoku.ui.actions.Action;

public class Application extends android.app.Application {

	public static final int ICON_ADD = R.drawable.onebit_1_31;
	public static final int ICON_REMOVE = R.drawable.onebit_1_32;
	public static final int ICON_CREATE = R.drawable.onebit_1_31;
	public static final int ICON_SAVE = R.drawable.onebit_1_34;
	public static final int ICON_DELETE = R.drawable.onebit_1_33;
	public static final int ICON_ACCEPT = R.drawable.onebit_1_34;
	public static final int ICON_DENY = R.drawable.onebit_1_33;
	public static final int ICON_SEARCH = R.drawable.onebit_1_02;
	public static final int ICON_EDIT = R.drawable.onebit_1_20;
	public static final int ICON_FAVORITE = R.drawable.onebit_1_44;
	public static final int ICON_UNFAVORITE = R.drawable.onebit_1_46;
	public static final int ICON_STATE_SCHEDULED = R.drawable.onebit_2_01;
	public static final int ICON_STATE_RESERVED = R.drawable.onebit_2_02;
	public static final int ICON_STATE_PREPARED = R.drawable.onebit_2_03;
	public static final int ICON_STATE_CONSUMED = R.drawable.onebit_2_04;
	public static final int ICON_CALENDAR = R.drawable.onebit_2_11;
	public static final int ICON_LIST = R.drawable.onebit_2_20;
	public static final int ICON_SETTINGS = R.drawable.onebit_2_21;
	public static final int ICON_GOALS = R.drawable.onebit_3_03;

	public static final int TEXTCOLOR_ERROR = Color.RED;
	public static final int TEXTCOLOR_BLACK = 0xff000000;
	public static final int TEXTCOLOR_GREY = 0xff404040;
	public static final int TEXTCOLOR_LIGHTGREY = 0xffb0b0b0;
	public static final int TEXTCOLOR_GREEN = 0xff008000;
	public static final int TEXTCOLOR_BLUE = 0xff000080;
	public static final int TEXTCOLOR_RED = 0xff800000;

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

	public void dispatch(Action action) {
		// TODO multithreading
		if (!action.isReady()) {
			throw new RuntimeException("Action "
					+ action.getClass().getSimpleName()
					+ " is not ready to be dispatched.");
		}
		action.run();
	}

}
