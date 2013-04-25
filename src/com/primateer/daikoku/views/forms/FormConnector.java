package com.primateer.daikoku.views.forms;

import android.view.View;

public interface FormConnector<T> extends Form<T> {

	public void register(final Class<T> type, View launcher);
	
}
