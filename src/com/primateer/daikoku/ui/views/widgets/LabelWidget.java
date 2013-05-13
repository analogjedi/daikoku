package com.primateer.daikoku.ui.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.primateer.daikoku.R;
import com.primateer.daikoku.ui.views.forms.InvalidDataException;

public class LabelWidget extends EditText {

	public LabelWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setHint(context.getString(R.string.label));
		this.setSingleLine();
	}

	public LabelWidget(Context context) {
		this(context,null);
	}
	
	public void validate() throws InvalidDataException {
		if (this.isEmpty()) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_label_empty));
		}
	}
	
	public boolean isEmpty() {
		return this.getText().length() < 1;
	}

	public void clear() {
		this.setText("");
	}
}
