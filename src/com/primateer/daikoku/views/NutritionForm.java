package com.primateer.daikoku.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.widgets.AmountWidget;

public class NutritionForm extends LinearLayout {

	private AmountWidget referenceAmount;
	private Button addButton;

	public NutritionForm(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.VERTICAL);

		referenceAmount = new AmountWidget(context);
		TextView referenceLabel = new TextView(context);
		referenceLabel.setText(Application.getContext().getString(
				R.string.reference_amount));
		referenceAmount.addView(referenceLabel, 0);
		referenceAmount.setUnits(UnitRegistry.getInstance().getUnitsByType(
				Unit.TYPE_MASS));
		referenceAmount.setAmount(NutrientRegistry.getInstance()
				.getDefaultReferenceAmount());

		addButton = new Button(context);
		addButton.setText("+");

		this.addView(referenceAmount);
		this.addView(addButton);
	}

	public NutritionForm(Context context) {
		this(context, null);
	}

}
