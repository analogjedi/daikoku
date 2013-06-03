package com.primateer.daikoku.ui.views.forms;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.ui.views.widgets.AmountWidget;

public class AmountForm extends LinearLayout implements Form<Amount> {
	
	AmountWidget widget;

	public AmountForm(Context context) {
		this(context, null);
	}

	public AmountForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);
		widget = new AmountWidget(context);
		this.addView(widget);
	}

	@Override
	public View getView() {
		return this;
	}

	@Override
	public void validate() throws InvalidDataException {
		widget.validate();
	}

	@Override
	public Amount getData() throws InvalidDataException {
		return widget.getData();
	}

	@Override
	public void setData(Amount data) throws IllegalArgumentException {
		widget.setData(data);
	}

	@Override
	public void clear() {
		widget.clear();
	}

	@Override
	public String getTitle() {
		return widget.getTitle();
	}
}
