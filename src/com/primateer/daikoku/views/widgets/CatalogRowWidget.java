package com.primateer.daikoku.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;

public class CatalogRowWidget<T> extends LinearLayout implements
		DataRowWidget<T> {

	protected T data;
	private ImageButton deleteButton;
	private ImageButton editButton;
	private Button selectButton;

	private SimpleObservable<DataRowWidget<T>> observable = new SimpleObservable<DataRowWidget<T>>();

	public CatalogRowWidget(Context context) {
		this(context, null);
	}

	public CatalogRowWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		deleteButton = new ImageButton(context);
		deleteButton.setImageResource(Application.ICON_DELETE);
		deleteButton.setBackgroundColor(Color.TRANSPARENT);
		LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.25f);
		deleteLayout.gravity = Gravity.CENTER;

		editButton = new ImageButton(context);
		editButton.setImageResource(Application.ICON_EDIT);
		editButton.setBackgroundColor(Color.TRANSPARENT);
		LinearLayout.LayoutParams editLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.25f);
		editLayout.gravity = Gravity.CENTER;

		selectButton = new Button(context);
		selectButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		selectButton.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1.5f));
		selectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				observable.notifyObservers(CatalogRowWidget.this);
			}
		});
		
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.addView(deleteButton,deleteLayout);
		this.addView(editButton,editLayout);
		this.addView(selectButton);
	}

	@Override
	public void setRowData(T data) {
		this.data = data;
		selectButton.setText(data.toString());
	}

	@Override
	public T getRowData() {
		return data;
	}

	@Override
	public void setOnDeleteRowListener(OnClickListener listener) {
		deleteButton.setOnClickListener(listener);
	}

	@Override
	public void addRowObserver(Observer<DataRowWidget<T>> observer) {
		observable.addObserver(observer);
	}

	@Override
	public void removeRowObserver(Observer<DataRowWidget<T>> observer) {
		observable.removeObserver(observer);
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
	public View getView() {
		return this;
	}
}