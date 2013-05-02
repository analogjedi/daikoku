package com.primateer.daikoku.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.model.Data;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.views.connector.FormDialogConnector;
import com.primateer.daikoku.views.lists.CatalogListAdapter;
import com.primateer.daikoku.views.widgets.Separator;

public class ListCatalog<T extends ValueObject> extends LinearLayout implements
		Catalog<T> {

	private Observer<T> selectionObserver;
	private ImageButton addButton;
	private ListView itemList;
	private CatalogListAdapter<T> listAdapter;
	private Class<T> dataClass;

	public ListCatalog(Context context) {
		this(context, null);
	}

	public ListCatalog(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		// add button
		addButton = new ImageButton(context);
		addButton.setImageResource(Application.ICON_ADD);

		// item list
		itemList = new ListView(context);
		itemList.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		itemList.setScrollContainer(false);
		itemList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		// composition
		this.addView(itemList);
		this.addView(new Separator(context));
		this.addView(addButton);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setDataClass(Class<T> dataClass) {
		this.dataClass = dataClass;
		listAdapter = new CatalogListAdapter<T>(dataClass, new Observer<T>() {
			@Override
			public void update(T item) {
				if (selectionObserver != null) {
					selectionObserver.update(item);
				}
			}
		});
		itemList.setAdapter(listAdapter);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FormDialogConnector<T> connector = new FormDialogConnector<T>();
				connector.register(ListCatalog.this.dataClass,
						ListCatalog.this.getContext());
				connector.addObserver(new Observer<T>() {
					@Override
					public void update(T item) {
						add(item);
						Data.getInstance().register(item);
					}
				});
				connector.showDialog();
			}
		});
		// load items from database
		for (ValueObject vo : Data.getInstance().getAll(dataClass)) {
			this.add((T) vo);
		}
	}

	@Override
	public void add(T item) {
		listAdapter.add(item);
	}

	@Override
	public View getView() {
		return this;
	}

	@Override
	public void setSelectionObserver(Observer<T> observer) {
		this.selectionObserver = observer;
	}
}
