package com.primateer.daikoku.ui.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.primateer.daikoku.Application;

public class AddButton extends ImageButton {

	public AddButton(Context context) {
		this (context,null);
	}

	public AddButton(Context context, AttributeSet attrs) {
		super(context,attrs);
		
		this.setImageResource(Application.ICON_ADD);
		this.setFocusable(false);
	}

}
