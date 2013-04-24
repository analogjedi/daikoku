package com.primateer.daikoku.views.widgets;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.views.forms.InvalidDataException;

public class NutrientRowWidget extends AmountWidget implements
		DataRowWidget<Nutrient> {

	private SimpleObservable<DataRowWidget<Nutrient>> widgetObservable = new SimpleObservable<DataRowWidget<Nutrient>>();
	private TextView label;
	private ImageButton delButton;
	private Nutrient.Type type;

	public NutrientRowWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		delButton = new ImageButton(context);
		delButton.setImageResource(Application.ICON_REMOVE);
		delButton.setBackgroundColor(Color.TRANSPARENT);
		LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.35f);
		deleteLayout.gravity = Gravity.CENTER;

		label = new TextView(context);
		label.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				0.7f));

		this.addView(delButton, 0, deleteLayout);
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

	private void setNutrientType(Nutrient.Type type) {
		this.label.setText(type.toString());
		this.type = type;
		List<Unit> units = UnitRegistry.getInstance().getUnitsByType(
				type.defaultUnit.type);
		this.setUnits(units);
		this.selectUnit(type.defaultUnit);
		widgetObservable.notifyObservers(this);
	}


	@Override
	public void storeRowPosition(int pos) {
		this.setTag(pos);
	}

	@Override
	public int restoreRowPosition() {
		return (Integer)this.getTag();
	}

	@Override
	public void setRowData(Nutrient nutrient) {
		this.setNutrientType(nutrient.type);
		super.setData(nutrient.amount);
	}

	@Override
	public Nutrient getRowData() throws InvalidDataException {
		return new Nutrient(type, getData());

	}

	@Override
	public void setOnDeleteRowListener(OnClickListener listener) {
		delButton.setOnClickListener(listener);
	}

	@Override
	public void addRowObserver(Observer<DataRowWidget<Nutrient>> observer) {
		widgetObservable.addObserver(observer);
	}

	@Override
	public void removeRowObserver(Observer<DataRowWidget<Nutrient>> observer) {
		widgetObservable.removeObserver(observer);
	}
}
