package com.primateer.daikoku.ui.views.forms;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.ShoppingList;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.ShoppingItem;
import com.primateer.daikoku.ui.actions.CatalogAction;
import com.primateer.daikoku.ui.views.lists.DataRowListAdapter;
import com.primateer.daikoku.ui.views.widgets.AddButton;
import com.primateer.daikoku.ui.views.widgets.row.DataRowWidget;

public class ShoppingListForm extends VoForm<ShoppingList> {

	private class ShoppingItemRowWidget extends LinearLayout implements
			DataRowWidget<ShoppingItem> {

		private SimpleObservable<DataRowWidget<ShoppingItem>> observable = new SimpleObservable<DataRowWidget<ShoppingItem>>();
		private ImageButton deleteButton;
		private TextView amountView;
		private TextView productView;
		private CheckBox checkBox;
		private ShoppingItem item;

		public ShoppingItemRowWidget(Context context, AttributeSet attrs) {
			super(context, attrs);

			deleteButton = new ImageButton(context);
			deleteButton.setImageResource(Application.ICON_REMOVE);
			deleteButton.setBackgroundColor(Color.TRANSPARENT);
			LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f);
			deleteLayout.gravity = Gravity.CENTER;

			amountView = new TextView(context);
			LinearLayout.LayoutParams amountLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.4f);
			amountLayout.gravity = Gravity.CENTER;

			productView = new TextView(context);
			LinearLayout.LayoutParams productLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.2f);
			productLayout.gravity = Gravity.CENTER;

			checkBox = new CheckBox(context);
			checkBox.setPadding(0, 0, 5, 0);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					item.setChecked(isChecked);
				}
			});
			LinearLayout.LayoutParams checkBoxLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f);
			checkBoxLayout.gravity = Gravity.CENTER;

			this.addView(deleteButton, deleteLayout);
			this.addView(amountView, amountLayout);
			this.addView(productView, productLayout);
			this.addView(checkBox, checkBoxLayout);
		}

		public ShoppingItemRowWidget(Context context) {
			this(context, null);
		}

		@Override
		public View getView() {
			return this;
		}

		@Override
		public void storeRowPosition(int pos) {
			this.setTag(pos);
		}

		@Override
		public int restoreRowPosition() {
			return (Integer) this.getTag();
		}

		@Override
		public void setRowData(ShoppingItem data) {
			this.item = data;
			amountView.setText(data.getAmount().toString());
			productView.setText(data.getProduct().toString());
			checkBox.setChecked(data.isChecked());
		}

		@Override
		public ShoppingItem getRowData() throws InvalidDataException {
			return item;
		}

		@Override
		public void setOnDeleteRowListener(OnClickListener listener) {
			deleteButton.setOnClickListener(listener);
		}

		@Override
		public void addRowObserver(
				Observer<DataRowWidget<ShoppingItem>> observer) {
			observable.addObserver(observer);
		}

		@Override
		public void removeRowObserver(
				Observer<DataRowWidget<ShoppingItem>> observer) {
			observable.removeObserver(observer);
		}
	}

	private class ShoppingListAdapter extends DataRowListAdapter<ShoppingItem> {
		@Override
		protected DataRowWidget<ShoppingItem> newWidget(Context context) {
			return new ShoppingItemRowWidget(context);
		}

		public void add(Product product) {
			super.add(new ShoppingItem(product));
		}
	}

	private ListView listView;
	private ShoppingListAdapter listAdapter;
	private AddButton addButton;

	public ShoppingListForm(Context context, AttributeSet attrs) {
		super(context, attrs);

		listView = new ListView(context);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listAdapter = new ShoppingListAdapter();
		listView.setAdapter(listAdapter);

		addButton = new AddButton(context);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Catalog<Product> catalog = new Catalog<Product>(Product.class);
				catalog.addObserver(new Observer<Product>() {
					@Override
					public void update(Product product) {
						listAdapter.add(product);
					}
				});
				CatalogAction<Product> action = new CatalogAction<Product>(
						getContext(), catalog, getResources().getString(
								R.string.title_pick_product));
				Application.getInstance().dispatch(action);
			}
		});

		this.addView(listView);
		this.addView(addButton);
	}

	public ShoppingListForm(Context context) {
		this(context, null);
	}

	@Override
	public void validate() throws InvalidDataException {
		// nothing to validate
	}

	@Override
	public void clear() {
		listAdapter.clear();
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.title_activity_shopping);
	}

	@Override
	protected ShoppingList gatherData() throws InvalidDataException {
		return new ShoppingList(listAdapter.getData());
	}

	@Override
	protected void fillFields(ShoppingList data)
			throws IllegalArgumentException {
		listAdapter.setData(data.getItemList());
	}

}
