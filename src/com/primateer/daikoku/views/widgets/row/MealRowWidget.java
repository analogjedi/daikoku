package com.primateer.daikoku.views.widgets.row;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.vos.Meal;
import com.primateer.daikoku.views.connector.FormDialogConnector;
import com.primateer.daikoku.views.forms.InvalidDataException;

public class MealRowWidget extends LinearLayout implements DataRowWidget<Meal> {

	private Meal data;
	private ImageButton deleteButton;
	private ImageButton editButton;
	private TextView label;
	private FormDialogConnector<Meal> formConnector;
	
	public MealRowWidget(Context context) {
		this(context, null);
	}

	public MealRowWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeRowPosition(int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public int restoreRowPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRowData(Meal data) {
		// TODO Auto-generated method stub

	}

	@Override
	public Meal getRowData() throws InvalidDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOnDeleteRowListener(OnClickListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRowObserver(Observer<DataRowWidget<Meal>> observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRowObserver(Observer<DataRowWidget<Meal>> observer) {
		// TODO Auto-generated method stub

	}

}
