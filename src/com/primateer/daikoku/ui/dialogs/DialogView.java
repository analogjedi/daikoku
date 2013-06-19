package com.primateer.daikoku.ui.dialogs;

import android.view.View;

import com.primateer.daikoku.model.Event;

public interface DialogView extends Event.Registry  {
	static final class DismissedEvent extends Event {
	}
	
	String getTitle();
	View getView();
	
}
