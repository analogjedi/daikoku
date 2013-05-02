package com.primateer.daikoku.views.lists;

import android.content.Context;

import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.views.widgets.row.DataRowWidget;
import com.primateer.daikoku.views.widgets.row.MealRowWidget;

public class MealListAdapter extends DataRowListAdapter<Meal> {

	@Override
	protected DataRowWidget<Meal> newWidget(Context context) {
		return new MealRowWidget(context);
//		return new CatalogRowWidget<Meal>(context);
	}

}
