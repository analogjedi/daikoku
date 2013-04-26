package com.primateer.daikoku.views.connector;

import android.content.Context;
import android.view.View;

public interface Connector<T> {

	void showDialog();
	void register(Class<T> dataClass, View launcher);
	void register(Class<T> dataClass, Context context);
}
