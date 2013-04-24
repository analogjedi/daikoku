package com.primateer.daikoku.views.widgets;

import android.view.View;
import android.view.View.OnClickListener;

import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.views.forms.InvalidDataException;

public interface DataRowWidget<T> {

	View getView();
	void storeRowPosition(int pos);
	int restoreRowPosition();
	void setRowData(T data);
	T getRowData() throws InvalidDataException;
	void setOnDeleteRowListener(OnClickListener listener);
	void addRowObserver(Observer<DataRowWidget<T>> observer);
	void removeRowObserver(Observer<DataRowWidget<T>> observer);
}
