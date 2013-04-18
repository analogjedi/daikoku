package com.primateer.daikoku.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.views.lists.NutrientListAdapter;
import com.primateer.daikoku.widgets.AmountWidget;
import com.primateer.daikoku.widgets.Separator;

public class NutritionForm extends LinearLayout {

	private AmountWidget referenceAmount;
	private ListView nutrientList;
	private Button addButton;

	public NutritionForm(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.VERTICAL);

		referenceAmount = new AmountWidget(context);
		TextView referenceLabel = new TextView(context);
		referenceLabel.setText(Application.getContext().getString(
				R.string.reference_amount));
		LayoutParams labelLayout = new LayoutParams(0,LayoutParams.WRAP_CONTENT,1.05f);
		labelLayout.gravity = Gravity.CENTER;
		referenceLabel.setLayoutParams(labelLayout);
		referenceLabel.setPadding(5, 0, 0, 0);
		referenceAmount.addView(referenceLabel, 0);
		referenceAmount.setUnits(UnitRegistry.getInstance().getAllUnits());
		referenceAmount.setAmount(NutrientRegistry.getInstance()
				.getDefaultReferenceAmount());

		nutrientList = new ListView(context);
		NutrientListAdapter nutrientAdapter = new NutrientListAdapter();
		for (Nutrient.Type type : NutrientRegistry.getInstance()
				.getDefaultNutritionFields()) {
			nutrientAdapter.add(type);
		}
		nutrientList.setAdapter(nutrientAdapter);

		addButton = new Button(context);
		addButton.setText("+");

		this.addView(referenceAmount);
		this.addView(new Separator(context));
		this.addView(nutrientList);
		this.addView(new Separator(context));
		this.addView(addButton);
	}

	public NutritionForm(Context context) {
		this(context, null);
	}

}
