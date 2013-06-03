package com.primateer.daikoku.ui.views.widgets.row;

import android.view.View;

import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.ui.views.forms.InvalidDataException;

public interface DataRowWidget<T> extends Event.Registry {
	
	public static class DeleteRequestEvent extends Event {
	}
	
	public static class ChangedEvent extends Event {
	}

	View getView();
	void storeRowPosition(int pos);
	int restoreRowPosition();
	void setRowData(T data);
	T getRowData() throws InvalidDataException;
}
