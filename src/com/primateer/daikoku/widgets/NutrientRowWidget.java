package com.primateer.daikoku.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.TextView;

public class NutrientRowWidget extends AmountWidget {

	private TextView label;
	private ImageButton delButton;
	
	public NutrientRowWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		delButton = new ImageButton(context);
		delButton.setImageResource(android.R.drawable.ic_delete);
//		delButton.setBackgroundColor(Color.TRANSPARENT);
		
		label = new TextView(context);
		
		this.addView(delButton, 0);
		this.addView(label, 1);
	}
	
	public NutrientRowWidget(Context context) {
		this(context,null);
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}
	
	public void setOnDeleteListener(OnClickListener listener) {
		delButton.setOnClickListener(listener);
	}
}
