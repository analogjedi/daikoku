package com.primateer.daikoku.ui.views.widgets;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListWidget extends LinearLayout {

	private static class EmptyView extends TextView {

		public EmptyView(Context context) {
			super(context);
			this.setVisibility(View.GONE);
			this.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			this.setText(R.string.placeholder_empty);
			this.setTextColor(Application.TEXTCOLOR_LIGHTGREY);
			this.setPadding(0, 10, 0, 10);
		}
	}

	private ListView listView;

	public ListWidget(Context context) {
		this(context, null);
	}

	public ListWidget(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));

		listView = new ListView(context);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listView.setScrollContainer(false);
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		EmptyView emptyView = new EmptyView(context);
		listView.setEmptyView(emptyView);

		this.addView(emptyView);
		this.addView(listView);
	}

	public void setAdapter(ListAdapter adapter) {
		listView.setAdapter(adapter);
	}
}
