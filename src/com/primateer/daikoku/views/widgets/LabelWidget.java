package com.primateer.daikoku.views.widgets;

import com.primateer.daikoku.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class LabelWidget extends EditText {

	public LabelWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setHint(context.getString(R.string.label));
		this.setSingleLine();
	}

	public LabelWidget(Context context) {
		this(context,null);
	}
}
