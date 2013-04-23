package com.primateer.daikoku.views.lists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.database.DataSetObserver;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.views.forms.InvalidDataException;
import com.primateer.daikoku.views.widgets.NutrientRowWidget;

public class NutrientListAdapter implements ListAdapter, OnClickListener {

	private List<Nutrient> data = new ArrayList<Nutrient>();
	private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();
	private Set<Nutrient.Type> occupiedTypes = new HashSet<Nutrient.Type>();

	public void add(Nutrient nutrient) {
		if (occupiedTypes.contains(nutrient.type)) {
			throw new IllegalArgumentException("Cannot add " + nutrient.type
					+ ": already occupied");
		}
		data.add(nutrient);
		occupiedTypes.add(nutrient.type);
		notifyObservers();				
	}
	
	public Nutrient add(Nutrient.Type type) {
		Nutrient nutrient = new Nutrient(type, new Amount(0, UnitRegistry
				.getInstance().getDefaultUnitByType(type.unitType)));
		add(nutrient);
		return nutrient;
	}

	public void clear() {
		data.clear();
		occupiedTypes.clear();
		notifyObservers();
	}
	
	public void remove(Nutrient nutrient) {
		int pos = data.indexOf(nutrient);
		data.remove(pos);
		occupiedTypes.remove(nutrient.type);
		notifyObservers();
	}

	public List<Nutrient> getNutrients() {
		return data;
	}

	public boolean isTypeOccupied(Nutrient.Type type) {
		return occupiedTypes.contains(type);
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
			NutrientRowWidget widget = new NutrientRowWidget(
					parent.getContext());
			widget.setNutrient(data.get(position));
			widget.setOnDeleteListener(NutrientListAdapter.this);
			widget.setTag(position);
			widget.addWidgetObserver(new Observer<NutrientRowWidget>() {
				@Override
				public void update(NutrientRowWidget observable) {
					int index = (Integer) observable.getTag();
					try {
						data.set(index, observable.getNutrient());
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
		this.remove(data.get(index));
	}

}
