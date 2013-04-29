package com.primateer.daikoku.views.widgets;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;

public class UnitsAmountWidget extends AmountWidget {

	private TextView label;

	public UnitsAmountWidget(Context context) {
		super(context);
		label = new TextView(context);
		label.setText(Application.getContext().getString(R.string.units));
		LayoutParams labelLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1.05f);
		labelLayout.gravity = Gravity.CENTER;
		label.setLayoutParams(labelLayout);
		label.setPadding(5, 0, 0, 0);
		this.addView(label, 0);
		this.setUnits(UnitRegistry.getInstance()
				.getUnitsByType(Unit.Type.COUNT));
		this.setData(new Amount(0, UnitRegistry.getInstance()
				.getDefaultUnitByType(Unit.Type.COUNT)));
	}

	public void setLabelText(int stringId) {
		label.setText(Application.getContext().getString(stringId));
	}
}
