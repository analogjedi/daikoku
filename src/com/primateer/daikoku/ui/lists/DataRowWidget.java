package com.primateer.daikoku.ui.lists;

import android.view.View;

import com.primateer.daikoku.Event;
import com.primateer.daikoku.ui.forms.Form;

public interface DataRowWidget<T> extends Event.Registry {
	
	public static class DeleteRequestEvent extends Event {
	}
	
	public static class ChangedEvent extends Event {
	}

	View getView();
	void storeRowPosition(int pos);
	int restoreRowPosition();
	void setRowData(T data);
	T getRowData() throws Form.InvalidDataException;
}
