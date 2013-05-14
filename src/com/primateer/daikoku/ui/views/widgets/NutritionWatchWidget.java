package com.primateer.daikoku.ui.views.widgets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Amount.UnitConversionException;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.NutritionHolder;

public class NutritionWatchWidget extends TextView {

	private static final int BG_COLOR = 0xffe0e0e0;
	// private static final int DEFAULT_COLOR = 0xff000000;

	private List<Nutrient.Type> watchList;
	private List<Goal> goals;
	private NutritionHolder subject;

	public NutritionWatchWidget(Context context, List<Nutrient.Type> watchList) {
		super(context, null);
		this.setBackgroundColor(BG_COLOR);
		this.setWatchList(watchList);
		this.update(null);
	}

	public NutritionWatchWidget(Context context) {
		this(context, NutrientRegistry.getInstance().getWatchList());
	}

	public void setWatchList(List<Nutrient.Type> watchList) {
		this.watchList = watchList;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
		this.update(this.subject);
	}

	public void update(NutritionHolder subject) {
		this.subject = subject;
		StringBuilder sb = new StringBuilder();

		Map<Nutrient.Type, Goal.Status> status = new HashMap<Nutrient.Type, Goal.Status>();
		for (Nutrient.Type type : watchList) {
			status.put(type, Goal.Status.UNRATED);
		}

		if (goals != null && subject != null) {
			for (Goal goal : goals) {
				try {
					Goal.Status match = goal.match(subject
							.getNutrition(goal.nutrientType));
					if (match.ordinal() > status.get(goal.nutrientType)
							.ordinal()) {
						status.put(goal.nutrientType, match);
					}
				} catch (UnitConversionException e) {
					Helper.logErrorStackTrace(this, e, "Unable to match goal");
				}
			}
		}

		for (Iterator<Nutrient.Type> it = watchList.iterator(); it.hasNext();) {
			Nutrient.Type type = it.next();
			try {
				Amount amount = (subject != null) ? subject.getNutrition(type)
						: type.getNullAmount();
				sb.append("<font color='").append(status.get(type).color)
						.append("'><b>").append(type.getAbbrev())
						.append("</b> ").append(amount.toRoundString())
						.append("</font>");
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
