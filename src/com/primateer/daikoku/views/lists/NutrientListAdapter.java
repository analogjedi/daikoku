package com.primateer.daikoku.views.lists;

import android.content.Context;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientSet;
import com.primateer.daikoku.views.widgets.DataRowWidget;
import com.primateer.daikoku.views.widgets.NutrientRowWidget;

public class NutrientListAdapter extends DataRowListAdapter<Nutrient> {

	NutrientSet nutrients;

	public NutrientListAdapter() {
		setNutrients(new NutrientSet());
	}
	
	@Override
	protected DataRowWidget<Nutrient> newWidget(Context context) {
		return new NutrientRowWidget(context);
	}

	public void setNutrients(NutrientSet nutrients) {
		this.nutrients = nutrients;
		super.data = nutrients;
		notifyObservers();
	}

	public Nutrient add(Nutrient.Type type) {
		Nutrient nutrient = new Nutrient(type, new Amount(0, type.defaultUnit));
		add(nutrient);
		return nutrient;
	}

	public NutrientSet getNutrients() {
		return nutrients;
	}

	public boolean isTypeOccupied(Nutrient.Type type) {
		return nutrients.isTypeOccupied(type);
	}
}
