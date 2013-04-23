package com.primateer.daikoku.views.widgets;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Observable;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.views.forms.Form;
import com.primateer.daikoku.views.forms.InvalidDataException;

public class AmountWidget extends LinearLayout implements Observable<Amount>,
		Form<Amount> {

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
					observable.notifyObservers(getData());
				} catch (IllegalArgumentException e) {
					// do not update with incorrect input
				} catch (NullPointerException e) {
					// do not update when empty
				} catch (InvalidDataException e) {
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

	public void selectUnit(Unit unit) {
		unitView.setSelection(units.indexOf(unit));
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
		// FIXME: different view for dropdown selection
		ArrayAdapter<Unit> adapter = new ArrayAdapter<Unit>(this.getContext(),
				android.R.layout.simple_spinner_item, units);
		unitView.setAdapter(adapter);
		selectUnit(units.get(0));
	}

	@Override
	public void addObserver(Observer<Amount> observer) {
		observable.addObserver(observer);
	}

	@Override
	public void removeObserver(Observer<Amount> observer) {
		observable.removeObserver(observer);
	}

	@Override
	public void validate() throws InvalidDataException {
		getData();
	}

	@Override
	public Amount getData() throws InvalidDataException {
		try {
			return new Amount(valueView.getText().toString()
					+ unitView.getSelectedItem().toString());
		} catch (Exception e) {
			throw new InvalidDataException(e.getMessage());
		}
	}

	@Override
	public void setData(Amount data) throws IllegalArgumentException {
		if (this.units == null) {
			throw new IllegalArgumentException(
					"Call setUnits() before setAmount()");
		}
		valueView.setText(String.valueOf(data.value));
		unitView.setSelection(units.indexOf(data.unit));
	}

	@Override
	public View getView() {
		return this;
	}

	@Override
	public void wipe() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.amount);
	}

}
