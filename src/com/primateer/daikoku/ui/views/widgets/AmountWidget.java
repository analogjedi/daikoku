package com.primateer.daikoku.ui.views.widgets;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Observable;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.ui.views.forms.Form;
import com.primateer.daikoku.ui.views.forms.InvalidDataException;

public class AmountWidget extends LinearLayout implements Observable<Amount>,
		Form<Amount> {

	private EditText valueView;
	private Spinner unitView;
	private List<Unit> units;
	private double value = 0;
	private SimpleObservable<Amount> observable = new SimpleObservable<Amount>();

	public AmountWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.HORIZONTAL);

		valueView = new EditText(context);
		valueView.setInputType(EditorInfo.TYPE_CLASS_NUMBER
				| EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
		LayoutParams valueLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.8f);
		valueLayout.gravity = Gravity.CENTER_VERTICAL;
		valueView.setText(String.valueOf(value));
		valueView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				return updateValueView();
			}
		});
		valueView.setOnFocusChangeListener(new TextView.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				updateValueView();
			}
		});

		unitView = new Spinner(context);
		LayoutParams unitLayout = new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				1.0f);
		unitLayout.gravity = Gravity.CENTER_VERTICAL;
		unitView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				updateValueView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		this.addView(valueView,valueLayout);
		this.addView(unitView,unitLayout);
	}

	public AmountWidget(Context context) {
		this(context, null);
	}
	
	private boolean updateValueView() {
		try {
			Amount amount = getData();
			value = amount.value;
			valueView.setText(String.valueOf(value));
			observable.notifyObservers(amount);
			return true;
		} catch (InvalidDataException e) {
			valueView.setText(String.valueOf(value));
			return false;
		}
	}

	public void selectUnit(Unit unit) {
		unitView.setSelection(units.indexOf(unit));
	}
	
	public void setUnits(Unit.Type type) {
		this.setUnits(UnitRegistry.getInstance().getUnitsByType(type));
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
					"AmountWidget: Call setUnits() before setData()");
		}
		valueView.setText(String.valueOf(data.value));
		unitView.setSelection(units.indexOf(data.unit));
	}

	@Override
	public View getView() {
		return this;
	}

	@Override
	public void clear() {
		this.setData(new Amount(0, UnitRegistry.getInstance()
				.getDefaultUnitByType(units.get(0).type)));
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.amount);
	}

}
