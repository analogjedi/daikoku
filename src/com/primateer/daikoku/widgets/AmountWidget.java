package com.primateer.daikoku.widgets;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.primateer.daikoku.model.Amount;

public class AmountWidget extends LinearLayout {

	private EditText valueView;
	private Spinner unitView;
	private List<String> units;

	public AmountWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.HORIZONTAL);

		valueView = new EditText(context);
		valueView.setInputType(EditorInfo.TYPE_CLASS_NUMBER
				| EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
		valueView.setText("0");

		unitView = new Spinner(context);

		this.addView(valueView);
		this.addView(unitView);
	}

	public AmountWidget(Context context) {
		this(context, null);
	}

	public void setUnits(List<String> units) {
		setUnits(units, units.get(0));
	}

	public void setUnits(List<String> units, String unit) {
		this.units = units;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getContext(), android.R.layout.simple_list_item_1, units);
		unitView.setAdapter(adapter);
		unitView.setSelection(units.indexOf(unit));
	}

	public void setAmount(Amount amount) {
		valueView.setText(String.valueOf(amount.value));
		unitView.setSelection(units.indexOf(amount.unit));
	}

	public Amount getAmount() {
		return new Amount(Double.parseDouble(valueView.getText().toString()),
				unitView.getSelectedItem().toString());
	}
}
