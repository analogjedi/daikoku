package com.primateer.daikoku.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.connector.FormDialogConnector;
import com.primateer.daikoku.views.lists.CatalogListAdapter;
import com.primateer.daikoku.views.widgets.Separator;

public class CatalogView<T extends ValueObject> extends LinearLayout {

	private ImageButton addButton;
	private ListView itemList;
	private CatalogListAdapter<T> listAdapter;

	public CatalogView(Context context, Catalog<T> catalog) {
		this(context, null, catalog);
	}

	private CatalogView(Context context, AttributeSet attrs, Catalog<T> catalog) {
		super(context, attrs);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// item list
		itemList = new ListView(context);
		itemList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		itemList.setScrollContainer(false);
		itemList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listAdapter = new CatalogListAdapter<T>(catalog);
		itemList.setAdapter(listAdapter);

		// add button
		addButton = new ImageButton(context);
		addButton.setImageResource(Application.ICON_ADD);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FormDialogConnector<T> connector = new FormDialogConnector<T>(
						listAdapter.getCatalog().dataClass, CatalogView.this
								.getContext());
				connector.addObserver(new Observer<T>() {
					@Override
					public void update(T item) {
						listAdapter.add(item);
					}
				});
				connector.showDialog();
			}
		});

		// composition
		this.addView(itemList);
		this.addView(new Separator(context));
		this.addView(addButton);
	}

	public void add(T item) {
		listAdapter.add(item);
	}

	public void reload() {
		listAdapter.reload();
	}
}
