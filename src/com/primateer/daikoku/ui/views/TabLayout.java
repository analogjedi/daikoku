package com.primateer.daikoku.ui.views;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Event.Listener;
import com.primateer.daikoku.ui.dialogs.DialogView;

public class TabLayout extends LinearLayout implements DialogView {

	private HashMap<String, View> tabs = new HashMap<String, View>();
	private HashMap<String, View> pages = new HashMap<String, View>();

	private LinearLayout tabLine;
	private String currentPage = null;
	private String title = null;

	private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();

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

	public void add(final DialogView page) {
		pages.put(page.getTitle(), page.getView());
		page.getView().setVisibility(View.GONE);
		page.addEventListener(DialogView.DismissedEvent.class, new Event.Pipe(
				dispatcher));
		this.addView(page.getView(), new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		Button tab = new Button(getContext());
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPage(page.getTitle());
			}
		});
		tab.setText(page.getTitle());
		tabs.put(page.getTitle(), tab);
		tabLine.addView(tab, new LayoutParams(0, LayoutParams.MATCH_PARENT,
				1.0f));

		selectPage(currentPage == null ? page.getTitle() : currentPage);
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

	@Override
	public void addEventListener(Class<? extends Event> type, Listener listener) {
		dispatcher.addEventListener(type, listener);
	}

	@Override
	public void removeEventListener(Class<? extends Event> type,
			Listener listener) {
		dispatcher.removeEventListener(type, listener);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return title == null ? currentPage : title;
	}

	@Override
	public View getView() {
		return this;
	}
}
