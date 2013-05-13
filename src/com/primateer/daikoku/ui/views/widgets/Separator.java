package com.primateer.daikoku.ui.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class Separator extends View {

	public Separator(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,1));
		this.setBackgroundColor(Color.GRAY);
	}
	
	public Separator(Context context) {
		this(context,null);
	}
}
