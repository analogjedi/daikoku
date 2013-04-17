package com.primateer.daikoku.widgets;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;

public class AmountWidget extends LinearLayout {

	private EditText valueView;
	private Spinner unitView;
	private List<String> units;

	public AmountWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.HORIZONTAL);

		((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.widget_amount, this);

		valueView = (EditText) this.findViewById(R.id.widget_amount_value);
		unitView = (Spinner) this.findViewById(R.id.widget_amount_unit);
		
		valueView.setText("0");
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
}
