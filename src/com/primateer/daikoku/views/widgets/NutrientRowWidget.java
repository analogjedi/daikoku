package com.primateer.daikoku.views.widgets;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.TextView;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;

public class NutrientRowWidget extends AmountWidget {

	private SimpleObservable<NutrientRowWidget> widgetObservable = new SimpleObservable<NutrientRowWidget>();
	private TextView label;
	private ImageButton delButton;
	private Nutrient.Type type;

	public NutrientRowWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		delButton = new ImageButton(context);
		delButton.setImageResource(android.R.drawable.ic_delete);
		delButton.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.35f));
		delButton.setBackgroundColor(Color.TRANSPARENT);
		// delButton.setP

		label = new TextView(context);
		label.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				0.7f));

		this.addView(delButton, 0);
		this.addView(label, 1);
		
		this.addObserver(new Observer<Amount>() {
			@Override
			public void update(Amount observable) {
				widgetObservable.notifyObservers(NutrientRowWidget.this);
			}
		});
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
		widgetObservable.notifyObservers(this);
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

	public void addWidgetObserver(Observer<NutrientRowWidget> observer) {
		widgetObservable.addObserver(observer);
	}

	public void removeWidgetObserver(Observer<NutrientRowWidget> observer) {
		widgetObservable.removeObserver(observer);
	}
}
