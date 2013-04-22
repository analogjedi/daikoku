package com.primateer.daikoku.widgets;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Observable;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.Unit;

public class AmountWidget extends LinearLayout implements Observable<Amount> {

	private EditText valueView;
	private Spinner unitView;
	private List<Unit> units;
	private SimpleObservable<Amount> observable = new SimpleObservable<Amount>();

	public AmountWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.HORIZONTAL);

		valueView = new EditText(context);
		valueView.setInputType(EditorInfo.TYPE_CLASS_NUMBER
				| EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
		valueView.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.9f));
		valueView.setText("0");
		valueView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					observable.notifyObservers(getAmount());
				} catch (IllegalArgumentException e) {
					// do not update with incorrect input
				}
			}
		});

		unitView = new Spinner(context);
		unitView.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				0.9f));

		this.addView(valueView);
		this.addView(unitView);
	}

	public AmountWidget(Context context) {
		this(context, null);
	}

	public void setUnits(List<Unit> units) {
		setUnits(units, units.get(0));
	}

	public void setUnits(List<Unit> units, Unit unit) {
		this.units = units;
		// FIXME: different view for dropdown selection
		ArrayAdapter<Unit> adapter = new ArrayAdapter<Unit>(this.getContext(),
				android.R.layout.simple_spinner_item, units);
		unitView.setAdapter(adapter);
		unitView.setSelection(units.indexOf(unit));
	}

	public void setAmount(Amount amount) {
		valueView.setText(String.valueOf(amount.value));
		unitView.setSelection(units.indexOf(amount.unit));
	}

	public Amount getAmount() {
		return new Amount(valueView.getText().toString()
				+ unitView.getSelectedItem().toString());
	}

	@Override
	public void addObserver(Observer<Amount> observer) {
		observable.addObserver(observer);
	}

	@Override
	public void removeObserver(Observer<Amount> observer) {
		observable.removeObserver(observer);
	}
}
