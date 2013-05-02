package com.primateer.daikoku.views.widgets;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.Goal;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.NutritionHolder;

public class NutritionWatchWidget extends TextView {

	private static final int BG_COLOR = 0xffe0e0e0;
	private static final int DEFAULT_COLOR = 0xff000000;

	private List<Nutrient.Type> watchList;
	private Map<Nutrient.Type, Goal> goals;
	private NutritionHolder subject;

	public static NutritionWatchWidget create(Context context) {
		return create(context, NutrientRegistry.getInstance().getWatchList());
	}

	public static NutritionWatchWidget create(Context context,
			List<Nutrient.Type> watchList) {
		NutritionWatchWidget result = new NutritionWatchWidget(context);
		result.setWatchList(watchList);
		result.update(null);
		return result;
	}

	private NutritionWatchWidget(Context context) {
		this(context, null);
	}

	private NutritionWatchWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setBackgroundColor(BG_COLOR);
	}

	public void setWatchList(List<Nutrient.Type> watchList) {
		this.watchList = watchList;
	}

	public void setGoals(Map<Nutrient.Type, Goal> goals) {
		this.goals = goals;
		this.update(this.subject);
	}

	public void update(NutritionHolder subject) {
		this.subject = subject;
		StringBuilder sb = new StringBuilder();
		for (Iterator<Nutrient.Type> it = watchList.iterator(); it.hasNext();) {
			Nutrient.Type type = it.next();
			try {
				Amount amount = (subject != null) ? subject.getNutrition(type)
						: type.getNullAmount();
				int color = DEFAULT_COLOR;
				if (goals != null) {
					Goal goal = goals.get(type);
					if (goal != null) {
						color = goal.match(amount).color;
					}
				}
				sb.append("<font color='").append(color).append("'><b>")
						.append(type.getAbbrev()).append("</b> ")
						.append(amount.toRoundString()).append("</font>");
			} catch (UnitConversionException e) {
				sb.append("<font color='").append(Application.TEXTCOLOR_ERROR)
						.append("'><b>").append(type.getAbbrev())
						.append("</b> ")
						.append(getResources().getString(R.string.error))
						.append("</font>");
				Helper.logErrorStackTrace(NutritionWatchWidget.this, e,
						"Unable to determine watched amount");
			}
			if (it.hasNext()) {
				sb.append(" | ");
			}
		}
		this.setText(Html.fromHtml(sb.toString()));
	}
}
