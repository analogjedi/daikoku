package com.primateer.daikoku.ui.views.widgets.row;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.views.connector.FormDialogConnector;

public class CatalogRowWidget<T extends ValueObject> extends LinearLayout
		implements DataRowWidget<T> {

	private T bufferedData;
	private ImageButton deleteButton;
	private ImageButton editButton;
	private TextView selectView;
	private FormDialogConnector<T> formConnector;

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

		selectView = new TextView(context);
		selectView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams selectLayout = new LayoutParams(0,
				LayoutParams.MATCH_PARENT, 1.5f);
		selectLayout.gravity = Gravity.CENTER_VERTICAL;
		selectView.setPadding(5, 0, 0, 0);

		this.setOrientation(LinearLayout.HORIZONTAL);
		this.addView(deleteButton, deleteLayout);
		this.addView(editButton, editLayout);
		this.addView(selectView, selectLayout);
	}

	public void setDataClass(Class<T> dataClass) { // TODO make this private
		formConnector = new FormDialogConnector<T>(dataClass, editButton);
		formConnector.addObserver(new Observer<T>() {
			@Override
			public void update(T data) {
				bufferedData = data;
				selectView.setText(data.toString());
			}
		});
	}

	@Override
	public void setRowData(T data) {
		formConnector.setData(data);
	}

	@Override
	public T getRowData() {
		return bufferedData;
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

	public void setSelectionObserver(final Observer<T> selectionObserver) {
		selectView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectionObserver.update(bufferedData);
			}
		});
	}
}