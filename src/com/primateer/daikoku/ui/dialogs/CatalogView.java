package com.primateer.daikoku.ui.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.primateer.daikoku.Event;
import com.primateer.daikoku.Event.Listener;
import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.ValueObject;
import com.primateer.daikoku.ui.lists.CatalogListAdapter;
import com.primateer.daikoku.ui.widgets.AddButton;
import com.primateer.daikoku.ui.widgets.ListWidget;
import com.primateer.daikoku.ui.widgets.Separator;

@SuppressLint("ViewConstructor")
public class CatalogView<T extends ValueObject> extends LinearLayout implements
		DialogView {

	private AddButton addButton;
	private ListWidget itemList;
	private CatalogListAdapter<T> listAdapter;
	private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();
	private String title;

	public CatalogView(Context context, Catalog<T> catalog) {
		this(context, null, catalog);
	}

	private CatalogView(Context context, AttributeSet attrs,
			final Catalog<T> catalog) {
		super(context, attrs);
		this.title = catalog.getTitle();

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
					FormFragment<T> fragment = new FormFragment<T>();
					fragment.setupForm(getContext(),
							listAdapter.getCatalog().dataClass);
					fragment.show(getContext());
				}
			});
		}

		// composition
		this.addView(itemList);
		this.addView(new Separator(context));
		if (catalog.isEditable()) {
			this.addView(addButton);
		}

		// register event listeners
		final Event.Listener dbListener = new Event.Listener() {
			@Override
			public void onEvent(Event event) {
				DBController.DBChangedEvent e = (DBController.DBChangedEvent) event;
				if (e.type.equals(catalog.dataClass)) {
					CatalogView.this.reload();
				}
			}
		};
		DBController.getInstance().addEventListener(
				DBController.DBChangedEvent.class, dbListener);
		catalog.addEventListener(Catalog.SelectionEvent.class, new Listener() {
			@Override
			public void onEvent(Event event) {
				DBController.getInstance().removeEventListener(
						DBController.DBChangedEvent.class, dbListener);
				dispatcher.dispatch(new DialogView.DismissedEvent());
			}
		});
		catalog.addEventListener(Catalog.TitleChangedEvent.class,
				new Listener() {
					@Override
					public void onEvent(Event event) {
						title = ((Catalog.TitleChangedEvent) event).title;
					}
				});
	}

	public void add(T item) {
		listAdapter.add(item);
	}

	public void reload() {
		listAdapter.reload();
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

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public View getView() {
		return this;
	}
}
