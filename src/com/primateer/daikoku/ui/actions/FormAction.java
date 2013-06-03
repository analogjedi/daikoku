package com.primateer.daikoku.ui.actions;

import android.content.Context;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Event.Listener;
import com.primateer.daikoku.ui.dialogs.FormFragment;
import com.primateer.daikoku.ui.views.connector.FormDialogConnector;

public class FormAction<T> implements Action, Event.Registry {

	private T data;
	private Context context;
	private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();

	public FormAction(Context context, T data) {
		this.context = context;
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		final FormDialogConnector<T> formConnector = new FormDialogConnector<T>(
				(Class<T>) data.getClass(), context);
		formConnector.setData(data);
		formConnector.addEventListener(FormFragment.AcceptEvent.class,
				new Event.Pipe(dispatcher));
		formConnector.showDialog();
	}

	@Override
	public int getIcon() {
		return Application.ICON_EDIT;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void addEventListener(Class<? extends Event> type, Listener listener) {
		dispatcher.addEventListener(type, listener);
	}

	@Override
	public void removeEventListener(Class<? extends Event> type,
			Listener listener) {
		dispatcher.removeEventListener(type, listener);
	}
}
