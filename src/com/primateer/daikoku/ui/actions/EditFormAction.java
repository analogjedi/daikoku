package com.primateer.daikoku.ui.actions;

import android.content.Context;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.views.connector.FormDialogConnector;

public class EditFormAction<T extends ValueObject> implements Action {

	private T vo;
	private Context context;

	public EditFormAction(Context context, T vo) {
		this.context = context;
		this.vo = vo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		final FormDialogConnector<T> formConnector = new FormDialogConnector<T>(
				(Class<T>) vo.getClass(), context);
		formConnector.setData(vo);
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
}
