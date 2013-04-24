package com.primateer.daikoku.views.lists;

import java.util.ArrayList;
import java.util.List;

import android.database.DataSetObserver;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.views.forms.InvalidDataException;
import com.primateer.daikoku.views.widgets.CatalogRowWidget;
import com.primateer.daikoku.views.widgets.DataRowWidget;

public class CatalogListAdapter<T> implements ListAdapter, OnClickListener {

	List<T> data = new ArrayList<T>();
	List<DataSetObserver> observers = new ArrayList<DataSetObserver>();
	
	
	public void add(T item) {
		data.add(item);
		notifyObservers();
	}
	
	public void clear() {
		data.clear();
		notifyObservers();
	}
	
	public void remove(T item) {
		data.remove(item);
		notifyObservers();
	}
	
	private void notifyObservers() {
		for (DataSetObserver observer : observers) {
			observer.onChanged();
		}
	}
	

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		observers.remove(observer);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).hashCode();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			CatalogRowWidget<T> widget = new CatalogRowWidget<T>(
					parent.getContext());
			widget.setRowData(data.get(position));
			widget.setOnDeleteRowListener(CatalogListAdapter.this);
			widget.storeRowPosition(position);
			widget.addRowObserver(new Observer<DataRowWidget<T>>() {
				@Override
				public void update(DataRowWidget<T> observable) {
					int index = observable.restoreRowPosition();
					try {
						data.set(index, observable.getRowData());
					} catch (InvalidDataException e) {
						Helper.logErrorStackTrace(this, e,
								"Unable to bind widget to list adapter");
					}
				}
			});
			return widget;
		} else {
			return convertView;
		}
	}

	@Override
	public int getItemViewType(int position) {
		return IGNORE_ITEM_VIEW_TYPE;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		int index = ((CatalogRowWidget<T>)v.getParent()).restoreRowPosition();
		this.remove(data.get(index));
	}

}
