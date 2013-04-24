package com.primateer.daikoku.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;

public class CatalogRowWidget<T> extends LinearLayout implements
		DataRowWidget<T> {

	private SimpleObservable<DataRowWidget<T>> observable = new SimpleObservable<DataRowWidget<T>>();

	public CatalogRowWidget(Context context) {
		this(context, null);
	}

	public CatalogRowWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setRowData(T data) {
		// TODO
	}

	@Override
	public T getRowData() {
		// TODO
		return null;
	}

	@Override
	public void setOnDeleteRowListener(OnClickListener listener) {
		// TODO
	}

	@Override
	public void addRowObserver(Observer<DataRowWidget<T>> observer) {
		observable.addObserver(observer);
	}

	@Override
	public void removeRowObserver(Observer<DataRowWidget<T>> observer) {
		observable.removeObserver(observer);
	}

	@Override
	public void storeRowPosition(int pos) {
		this.setTag(pos);
	}

	@Override
	public int restoreRowPosition() {
		return (Integer)this.getTag();
	}
}