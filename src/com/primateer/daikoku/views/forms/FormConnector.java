package com.primateer.daikoku.views.forms;

import android.content.Context;
import android.view.View;

public interface FormConnector<T> extends Form<T> {

	void showDialog();
	void register(Class<T> dataClass, View launcher);
	void register(Class<T> dataClass, Context context);
}
