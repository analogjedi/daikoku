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

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.widgets.NutrientRowWidget;

public class NutrientListAdapter implements ListAdapter, OnClickListener {

	private List<Nutrient> data = new ArrayList<Nutrient>();
	private List<NutrientRowWidget> widgets = new ArrayList<NutrientRowWidget>();
	private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();
	private Set<Nutrient.Type> occupiedTypes = new HashSet<Nutrient.Type>();	

	public Nutrient add(Nutrient.Type type) {
		if (occupiedTypes.contains(type)) {
			throw new IllegalArgumentException("Cannot add " + type
					+ ": already occupied");
		}
		Nutrient nutrient = new Nutrient(type, new Amount(0, UnitRegistry
				.getInstance().getDefaultUnitByType(type.unitType)));
		data.add(nutrient);
		occupiedTypes.add(type);
		notifyObservers();
		return nutrient;
	}

	public void remove(Nutrient nutrient) {
		int pos = data.indexOf(nutrient);
		data.remove(pos);
		widgets.remove(pos);
		occupiedTypes.remove(nutrient.type);
		notifyObservers();
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
//			widget.add
			widgets.add(position, widget);
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
		int position = (Integer) ((View) v.getParent()).getTag();
		// Toast.makeText(v.getContext(),
		// (CharSequence) data.get(position).type.getName(),
		// Toast.LENGTH_SHORT).show();
		this.remove(data.get(position));
	}

}
