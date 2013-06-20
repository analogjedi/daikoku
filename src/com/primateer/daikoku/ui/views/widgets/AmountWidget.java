package com.primateer.daikoku.ui.views.widgets;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Catalog.Loader;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.ui.dialogs.CatalogView;
import com.primateer.daikoku.ui.views.connector.DialogConnector;
import com.primateer.daikoku.ui.views.forms.Form;
import com.primateer.daikoku.ui.views.forms.InvalidDataException;

public class AmountWidget extends Form<Amount> {

	private class UnitSelector extends Button {
		private Unit unit;
		private List<Unit> units;

		public UnitSelector(Context context) {
			super(context);
			this.setFocusable(false);
			this.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Catalog<Unit> catalog = new Catalog<Unit>(Unit.class);
					catalog.setEditable(false);
					catalog.setLoader(new Loader<Unit>() {
						@Override
						public Collection<Unit> load(Catalog<Unit> catalog) {
							return units;
						}
					});
					catalog.addEventListener(Catalog.SelectionEvent.class,
							new Event.Listener() {
								@SuppressWarnings("unchecked")
								@Override
								public void onEvent(Event event) {
									Catalog.SelectionEvent<Unit> ev = (Catalog.SelectionEvent<Unit>) event;
									setData(ev.selection);
									try {
										dispatcher
												.dispatch(new AmountChangedEvent(
														AmountWidget.this
																.getData()));
									} catch (InvalidDataException e) {
										Helper.logErrorStackTrace(this, e,
												"Unable to update UnitSelector");
									}
								}
							});
					DialogConnector connector = new DialogConnector(
							new CatalogView<Unit>(getContext(), catalog),
							getContext(), getResources().getString(
									R.string.title_pick_unit));
					connector.showDialog();
				}
			});
		}

		public Unit getData() {
			if (unit != null) {
				return unit;
			} else if (units != null) {
				return UnitRegistry.getInstance().getDefaultUnitByType(
						unit.type);
			}
			return null;
		}

		public void setData(Unit unit) {
			this.unit = unit;
			this.setText(unit.toString());
		}

		public void setUnits(List<Unit> units) {
			this.units = units;
		}

		public void setType(Unit.Type type) {
			this.units = UnitRegistry.getInstance().getUnitsByType(type);
		}
	}

	private class ValueField extends EditText {

		private double value;

		public ValueField(Context context) {
			super(context);

			this.setInputType(EditorInfo.TYPE_CLASS_NUMBER
					| EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
			this.setText(String.valueOf(value));
			this.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ValueField.this.setText("");
				}
			});
			this.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					getData();
					return false;
				}
			});
			this.setOnFocusChangeListener(new TextView.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						ValueField.this.setText("");
					} else {
						getData();
					}
				}
			});
		}

		public void setData(double value) {
			this.value = value;
			String text = String.valueOf(value);
			this.setText(text);
			this.setHint(text);
		}

		public double getData() {
			try {
				Amount amount = new Amount(getText().toString()
						+ Unit.UNIT_GRAM);
				if (amount.value != this.value) {
					this.value = amount.value;
					dispatcher.dispatch(new AmountChangedEvent(
							AmountWidget.this.getData()));
				}
			} catch (IllegalArgumentException e) {
				// invalid value string; do nothing
			} catch (InvalidDataException e) {
				Helper.logErrorStackTrace(this, e,
						"Unable to update ValueField");
			}
			return value;
		}
	}

	public static class AmountChangedEvent extends Event {
		public final Amount amount;

		public AmountChangedEvent(Amount amount) {
			this.amount = amount;
		}
	}

	private ValueField valueView;
	private UnitSelector unitView;

	public AmountWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.HORIZONTAL);

		valueView = new ValueField(context);
		LayoutParams valueLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.6f);
		valueLayout.gravity = Gravity.CENTER_VERTICAL;

		unitView = new UnitSelector(context);
		LayoutParams unitLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.4f);
		unitLayout.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

		this.addView(valueView, valueLayout);
		this.addView(unitView, unitLayout);
	}

	public AmountWidget(Context context) {
		this(context, null);
	}

	public void selectUnit(Unit unit) {
		unitView.setData(unit);
	}

	public void setUnits(Unit.Type type) {
		unitView.setType(type);
	}

	public void setUnits(List<Unit> units) {
		unitView.setUnits(units);
	}

	@Override
	public void validate() throws InvalidDataException {
		getData();
	}

	@Override
	public void clear() {
		this.setData(new Amount(0, UnitRegistry.getInstance()
				.getDefaultUnitByType(Unit.Type.UNSPECIFIED)));
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.amount);
	}

	@Override
	protected Amount gatherData() throws InvalidDataException {
		try {
			return new Amount(valueView.getData()
					+ unitView.getData().toString());
		} catch (Exception e) {
			throw new InvalidDataException(e.getMessage());
		}
	}

	@Override
	protected void fillFields(Amount data) throws IllegalArgumentException {
		valueView.setData(data.value);
		unitView.setData(data.unit);
		unitView.setType(data.unit.type);
	}
}
