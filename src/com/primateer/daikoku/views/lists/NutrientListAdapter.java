package com.primateer.daikoku.views.lists;

import java.util.ArrayList;
import java.util.List;

import android.database.DataSetObserver;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientSet;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.views.forms.InvalidDataException;
import com.primateer.daikoku.views.widgets.NutrientRowWidget;

public class NutrientListAdapter implements ListAdapter, OnClickListener {

	private NutrientSet nutrients = new NutrientSet();
	private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();

	public void setNutrients(NutrientSet nutrients) {
		this.nutrients = nutrients;
		notifyObservers();
	}
	
	public void add(Nutrient nutrient) {
		nutrients.add(nutrient);
		notifyObservers();				
	}
	
	public Nutrient add(Nutrient.Type type) {
		Nutrient nutrient = new Nutrient(type, new Amount(0, type.defaultUnit));
		add(nutrient);
		return nutrient;
	}

	public void clear() {
		nutrients.clear();
		notifyObservers();
	}
	
	public void remove(Nutrient nutrient) {
		nutrients.remove(nutrient);
		notifyObservers();
	}

	public NutrientSet getNutrients() {
		return nutrients;
	}

	public boolean isTypeOccupied(Nutrient.Type type) {
		return nutrients.isTypeOccupied(type);
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
		return nutrients.size();
	}

	@Override
	public Object getItem(int position) {
		return nutrients.get(position);
	}

	@Override
	public long getItemId(int position) {
		return nutrients.get(position).hashCode();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			NutrientRowWidget widget = new NutrientRowWidget(
					parent.getContext());
			widget.setNutrient(nutrients.get(position));
			widget.setOnDeleteListener(NutrientListAdapter.this);
			widget.setTag(position);
			widget.addWidgetObserver(new Observer<NutrientRowWidget>() {
				@Override
				public void update(NutrientRowWidget observable) {
					int index = (Integer) observable.getTag();
					try {
						nutrients.set(index, observable.getNutrient());
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
		return getCount() == 0;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public void onClick(View v) {
		int index = (Integer) ((View) v.getParent()).getTag();
		this.remove(nutrients.get(index));
	}

}
