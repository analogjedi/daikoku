package com.primateer.daikoku.ui.views.forms;

import android.content.Context;
import android.util.AttributeSet;

import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.ui.views.widgets.AmountWidget;

public class AmountForm extends Form<Amount> {
	
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
	public void validate() throws InvalidDataException {
		widget.validate();
	}

	@Override
	public void clear() {
		widget.clear();
	}

	@Override
	public String getTitle() {
		return widget.getTitle();
	}

	@Override
	protected Amount gatherData() throws InvalidDataException {
		return widget.getData();
	}

	@Override
	protected void fillFields(Amount data) throws IllegalArgumentException {
		widget.setData(data);
	}
}
