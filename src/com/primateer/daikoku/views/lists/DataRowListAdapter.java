package com.primateer.daikoku.views.lists;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.views.forms.InvalidDataException;
import com.primateer.daikoku.views.widgets.DataRowWidget;

public abstract class DataRowListAdapter<T> implements ListAdapter,
		View.OnClickListener {

	protected List<T> data = new ArrayList<T>();
	private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();

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

	protected void notifyObservers() {
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
			DataRowWidget<T> widget = newWidget(parent.getContext());
			widget.setRowData(data.get(position));
			widget.setOnDeleteRowListener(DataRowListAdapter.this);
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
			return widget.getView();
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
	protected T getItemFromView(View v) {
		return data.get(((DataRowWidget<T>) v.getParent()).restoreRowPosition());
	}

	@Override
	public void onClick(View v) {
		this.remove(getItemFromView(v));
	}


	/**
	 * Instantiate a new instance of the corresponding data row.
	 * 
	 * @param context
	 *            for the constructor
	 * @return the instantiated instance
	 */
	protected abstract DataRowWidget<T> newWidget(Context context);
}
