package com.primateer.daikoku.views.lists;

import java.util.ArrayList;
import java.util.List;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.widgets.NutrientRowWidget;

public class NutrientListAdapter implements ListAdapter {

	private List<Nutrient> data = new ArrayList<Nutrient>();
	private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();

	public void add(Nutrient.Type type) {
		Nutrient nutrient = new Nutrient(type, new Amount(0, UnitRegistry
				.getInstance().getDefaultUnitByType(type.unitType)));
		data.add(nutrient);
	}

	public void remove(Nutrient nutrient) {
		data.remove(nutrient);
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

}
