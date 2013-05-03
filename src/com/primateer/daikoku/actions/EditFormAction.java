package com.primateer.daikoku.actions;

import android.content.Context;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.connector.FormDialogConnector;

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
		FormDialogConnector<T> formConnector = new FormDialogConnector<T>(
				(Class<T>) vo.getClass(), context);
		formConnector.addObserver(new Observer<T>() {
			@Override
			public void update(T data) {
				vo = data;
				Data.getInstance().register(vo);
			}
		});
		formConnector.setData(vo);
		formConnector.showDialog();
	}

	@Override
	public int getIcon() {
		return Application.ICON_EDIT;
	}
}
