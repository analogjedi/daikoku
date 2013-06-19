package com.primateer.daikoku.ui.actions;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.primateer.daikoku.ui.dialogs.DialogView;
import com.primateer.daikoku.ui.views.connector.TabDialogConnector;

public class TabDialogAction implements Action {

	private Context context;
	private List<DialogView> pages = new ArrayList<DialogView>();
	private String title;

	public TabDialogAction(Context context, String title) {
		this.context = context;
		this.title = title;
	}

	public void add(DialogView page) {
		this.pages.add(page);
	}

	@Override
	public void run() {
		TabDialogConnector connector = new TabDialogConnector(pages, context,
				title);
		connector.showDialog();
	}

	@Override
	public boolean isReady() {
		return pages.size() > 1;
	}

	@Override
	public int getIcon() {
		return -1;
	}
}
