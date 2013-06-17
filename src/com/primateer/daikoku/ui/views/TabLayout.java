package com.primateer.daikoku.ui.views;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class TabLayout extends LinearLayout {

	private HashMap<String, View> tabs = new HashMap<String, View>();
	private HashMap<String, View> pages = new HashMap<String, View>();

	private LinearLayout tabLine;
	private String currentPage = null;

	public TabLayout(Context context) {
		this(context, null);
	}

	public TabLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);

		tabLine = new LinearLayout(context);
		tabLine.setOrientation(HORIZONTAL);

		this.addView(tabLine, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
	}

	public void selectPage(String selectedID) {
		for (String pageID : pages.keySet()) {
			if (pageID.equals(selectedID)) {
				pages.get(pageID).setVisibility(View.VISIBLE);
				((Button) tabs.get(pageID)).setTypeface(null, Typeface.BOLD);
			} else {
				pages.get(pageID).setVisibility(View.GONE);
				((Button) tabs.get(pageID)).setTypeface(null, Typeface.NORMAL);
			}
		}
		currentPage = selectedID;
	}

	public void addPage(final String title, View page) {
		pages.put(title, page);
		page.setVisibility(View.GONE);
		this.addView(page, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		Button tab = new Button(getContext());
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPage(title);
			}
		});
		tab.setText(title);
		tabs.put(title, tab);
		tabLine.addView(tab, new LayoutParams(0, LayoutParams.MATCH_PARENT,
				1.0f));

		selectPage(currentPage == null ? title : currentPage);
	}

	public void removePage(String title) {
		View page = pages.remove(title);
		if (page != null) {
			this.removeView(page);
			View tab = tabs.remove(title);
			tabLine.removeView(tab);
		}

		if (currentPage == title) {
			currentPage = null;
		}
		if (currentPage == null && pages.size() > 0) {
			for (String pageID : pages.keySet()) {
				selectPage(pageID);
				break;
			}
		}
	}
}
