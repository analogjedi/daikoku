package com.primateer.daikoku.views.widgets;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutritionHolder;

public class NutritionWatchWidget extends LinearLayout {

//	List<Nutrient.Type> watchList;
//	Map<Nutrient.Type,TextView> displays;
	
	public NutritionWatchWidget(Context context) {
		this(context,null);
	}

	public NutritionWatchWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void register(NutritionHolder subject, List<Nutrient.Type> watchList) {
		this.removeAllViews();
		for (Nutrient.Type type : watchList) {
			TextView display = new TextView(getContext());
			String amount;
			try {
				amount = subject.getNutrition(type).toString();
			} catch (UnitConversionException e) {
				amount = getResources().getString(R.string.error);
				e.printStackTrace();
			}
			display.setText(type.getAbbrev() + " " + amount);
			this.addView(display);
		}
	}
}
