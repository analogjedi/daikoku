package com.primateer.daikoku.ui.views.forms;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.primateer.daikoku.model.ValueObject;

public abstract class VoForm<T extends ValueObject> extends LinearLayout implements Form<T> {

	private static final int PADDING_LEFT = 50;
	
	private long dataId = -1;

	public VoForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.VERTICAL);
	}

	public VoForm(Context context) {
		this(context, null);
	}

	@Override
	public final View getView() {
		return this;
	}

	@Override
	public final T getData() throws InvalidDataException {
		validate();
		T data = gatherData();
		data.setId(dataId);
		return data;
	}
	
	@Override
	public final void setData(T data) throws IllegalArgumentException {
//		clear();
		if (data == null) {
			return;
		}
		dataId = data.getId();
		fillFields(data);
	}

	@Override
	public int getPaddingLeft() {
		ViewParent parent = this.getParent();
		if (parent instanceof View) { // FIXME ViewParents are never Views
			return ((View) parent).getPaddingLeft() + PADDING_LEFT;
		}
		return super.getPaddingLeft();
	}

	protected abstract T gatherData() throws InvalidDataException;
	protected abstract void fillFields(T data)
			throws IllegalArgumentException;
}
