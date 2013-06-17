package com.primateer.daikoku.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.views.connector.FormDialogConnector;
import com.primateer.daikoku.ui.views.lists.CatalogListAdapter;
import com.primateer.daikoku.ui.views.widgets.AddButton;
import com.primateer.daikoku.ui.views.widgets.ListWidget;
import com.primateer.daikoku.ui.views.widgets.Separator;

@SuppressLint("ViewConstructor")
public class CatalogView<T extends ValueObject> extends LinearLayout {

	private AddButton addButton;
	private ListWidget itemList;
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
		itemList = new ListWidget(context);
		listAdapter = new CatalogListAdapter<T>(catalog);
		itemList.setAdapter(listAdapter);

		if (catalog.isEditable()) {
			// add button
			addButton = new AddButton(context);
			addButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					FormDialogConnector<T> connector = new FormDialogConnector<T>(
							listAdapter.getCatalog().dataClass,
							CatalogView.this.getContext());
					connector.showDialog();
				}
			});
		}

		// composition
		this.addView(itemList);
		this.addView(new Separator(context));
		if (catalog.isEditable()) {
			this.addView(addButton);
		}
	}

	public void add(T item) {
		listAdapter.add(item);
	}

	public void reload() {
		listAdapter.reload();
	}
}
