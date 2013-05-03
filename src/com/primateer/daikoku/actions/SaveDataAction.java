package com.primateer.daikoku.actions;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.ValueObject;

public class SaveDataAction<T extends ValueObject> implements Action {

	private T item;

	public SaveDataAction(T item) {
		this.item = item;
	}

	@Override
	public void run() {
		Data.getInstance().register(item);
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public int getIcon() {
		return Application.ICON_SAVE;
	}

}