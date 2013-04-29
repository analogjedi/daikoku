package com.primateer.daikoku.views.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutritionHolder;

// TODO write a flow layout class with horizontal wrapping and extend from there
public class NutritionWatchWidget extends LinearLayout {

	List<Nutrient.Type> watchList;
	Map<Nutrient.Type, TextView> displays = new HashMap<Nutrient.Type, TextView>();

	public NutritionWatchWidget(Context context) {
		this(context, null);
	}

	public NutritionWatchWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setVisibility(INVISIBLE);
	}

	private void addSeparator() {
		TextView separator = new TextView(getContext());
		separator.setText("|");
		// separator.setPadding(10, 0, 10, 0);
		this.addView(separator);
	}

	public void setWatchList(List<Nutrient.Type> watchList) {
		this.watchList = watchList;
		this.removeAllViews();
		for (int i = 0; i < watchList.size(); i++) {
			addSeparator();
			TextView display = new TextView(getContext());
			this.addView(display);
			displays.put(watchList.get(i), display);
		}
		addSeparator();
	}

	public void update(NutritionHolder subject) {
		for (Nutrient.Type type : watchList) {
			String amount;
			try {
				amount = subject.getNutrition(type).toString();
			} catch (UnitConversionException e) {
				amount = getResources().getString(R.string.error);
				Helper.logErrorStackTrace(NutritionWatchWidget.this, e,
						"Unable to determine watched amount");
			}
			TextView display = displays.get(type);
			display.setText(Html.fromHtml("<b>" + type.getAbbrev() + "</b> "
					+ amount));
			// display.setTextColor(Color.GREEN);
		}
		this.setVisibility(VISIBLE);
	}
}
