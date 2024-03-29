package com.primateer.daikoku.ui.forms;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.Unit;

public class ReferenceAmountForm extends AmountForm {

	private TextView label;
	
	public ReferenceAmountForm(Context context) {
		super(context);
		label = new TextView(context);
		label.setText(Application.getContext().getString(R.string.reference_amount));
		LayoutParams labelLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1.05f);
		labelLayout.gravity = Gravity.CENTER;
		label.setLayoutParams(labelLayout);
		label.setPadding(5, 0, 0, 0);
		this.addView(label, 0);
		clear();
	}
	
	public void setLabelText(int stringId) {
		label.setText(Application.getContext().getString(stringId));		
	}
	
	@Override
	public void clear() {
		this.setData(NutrientRegistry.getInstance()
				.getDefaultReferenceAmount());		
	}
	
	@Override
	public void fillFields(Amount amount) {
		super.fillFields(amount);
		this.setUnits(Unit.Type.UNSPECIFIED);
	}
}
