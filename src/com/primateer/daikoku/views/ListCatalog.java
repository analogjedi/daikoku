package com.primateer.daikoku.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.forms.DialogFormConnector;
import com.primateer.daikoku.views.lists.CatalogListAdapter;
import com.primateer.daikoku.views.widgets.Separator;

public class ListCatalog<T extends ValueObject<T>> extends LinearLayout
		implements Catalog<T> {

	private ImageButton addButton;
	private ListView itemList;
	private CatalogListAdapter<T> listAdapter;

	public ListCatalog(Context context) {
		this(context, null);
	}

	public ListCatalog(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// add button
		addButton = new ImageButton(context);
		addButton.setImageResource(Application.ICON_ADD);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				// NutritionForm.this.showAddRowDialog();
			}
		});

		// item list
		itemList = new ListView(context);
		itemList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		itemList.setScrollContainer(false);
		itemList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listAdapter = new CatalogListAdapter<T>();
		itemList.setAdapter(listAdapter);

		// composition
		this.addView(itemList);
		this.addView(new Separator(context));
		this.addView(addButton);
	}

	@Override
	public void setClass(Class<T> dataClass) {
		listAdapter.setClass(dataClass);
	}
	@Override
	public void add(T item) {
		listAdapter.add(item);
	}
}
