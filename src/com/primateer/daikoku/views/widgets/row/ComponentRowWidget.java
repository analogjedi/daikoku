package com.primateer.daikoku.views.widgets.row;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Component;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.views.connector.CatalogDialogConnector;
import com.primateer.daikoku.views.forms.InvalidDataException;
import com.primateer.daikoku.views.widgets.AmountWidget;

public class ComponentRowWidget extends LinearLayout implements
		DataRowWidget<Component> {

	private Component component;
	private ImageButton deleteButton;
	private TextView selectView;
	private AmountWidget amountView;
	private SimpleObservable<DataRowWidget<Component>> observable = new SimpleObservable<DataRowWidget<Component>>();

	public ComponentRowWidget(Context context) {
		this(context, null);
	}

	public ComponentRowWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		deleteButton = new ImageButton(context);
		deleteButton.setImageResource(Application.ICON_REMOVE);
		deleteButton.setBackgroundColor(Color.TRANSPARENT);
		LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.08f);
		deleteLayout.gravity = Gravity.CENTER;

		selectView = new TextView(context);
		selectView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams selectLayout = new LayoutParams(0,
				LayoutParams.MATCH_PARENT, 0.42f);
		selectLayout.gravity = Gravity.CENTER_VERTICAL;
		selectView.setPadding(5, 0, 0, 5);
		selectView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CatalogDialogConnector<Product> connector = new CatalogDialogConnector<Product>(
						Product.class, getContext(), null);
				connector.setSelectionObserver(new Observer<Product>() {
					@Override
					public void update(Product item) {
						setRowData(new Component(item));
					}
				});
				connector.showDialog();
			}
		});

		amountView = new AmountWidget(context);
		LinearLayout.LayoutParams amountLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.6f);
		amountLayout.gravity = Gravity.CENTER;
		amountView.addObserver(new Observer<Amount>() {
			@Override
			public void update(Amount amount) {
				component = new Component(component.product, amount);
				observable.notifyObservers(ComponentRowWidget.this);
			}
		});

		this.addView(deleteButton, deleteLayout);
		this.addView(selectView, selectLayout);
		this.addView(amountView, amountLayout);
	}

	@Override
	public View getView() {
		return this;
	}

	@Override
	public void storeRowPosition(int pos) {
		this.setTag(pos);
	}

	@Override
	public int restoreRowPosition() {
		return (Integer) this.getTag();
	}

	@Override
	public void setRowData(Component data) {
		this.component = data;
		selectView.setText(data.product.toString());
		Set<Unit> permissibleUnits = new HashSet<Unit>();
		permissibleUnits.add(data.amount.unit);
		permissibleUnits.addAll(UnitRegistry.getInstance().getUnitsByType(
				data.amount.unit.type));
		if (data.product.getUnits() > 0) {
			permissibleUnits.add(Unit.UNIT_UNITS);
		}
		amountView.setUnits(new ArrayList<Unit>(permissibleUnits));
		amountView.setData(data.amount);
		observable.notifyObservers(this);
	}

	@Override
	public Component getRowData() throws InvalidDataException {
		return component;
	}

	@Override
	public void setOnDeleteRowListener(OnClickListener listener) {
		deleteButton.setOnClickListener(listener);
	}

	@Override
	public void addRowObserver(Observer<DataRowWidget<Component>> observer) {
		observable.addObserver(observer);
	}

	@Override
	public void removeRowObserver(Observer<DataRowWidget<Component>> observer) {
		observable.removeObserver(observer);
	}

}
