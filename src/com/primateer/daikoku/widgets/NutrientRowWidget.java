package com.primateer.daikoku.widgets;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.TextView;

import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;

public class NutrientRowWidget extends AmountWidget {

	private TextView label;
	private ImageButton delButton;
	private Nutrient.Type type;

	public NutrientRowWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		delButton = new ImageButton(context);
		delButton.setImageResource(android.R.drawable.ic_delete);
		// delButton.setBackgroundColor(Color.TRANSPARENT);

		label = new TextView(context);

		this.addView(delButton, 0);
		this.addView(label, 1);
	}

	public NutrientRowWidget(Context context) {
		this(context, null);
	}

	public void setNutrientType(Nutrient.Type type) {
		this.label.setText(type.toString());
		this.type = type;
		List<Unit> units = UnitRegistry.getInstance().getUnitsByType(
				type.unitType);
		Unit defaultUnit = UnitRegistry.getInstance().getDefaultUnitByType(
				type.unitType);
		this.setUnits(units, defaultUnit);
	}

	public void setOnDeleteListener(OnClickListener listener) {
		delButton.setOnClickListener(listener);
	}

	public void setNutrient(Nutrient nutrient) {
		setNutrientType(nutrient.type);
		setAmount(nutrient.amount);
	}

	public Nutrient getNutrient() {
		return new Nutrient(type, getAmount());
	}
}
