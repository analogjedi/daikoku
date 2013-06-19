package com.primateer.daikoku.ui.views.forms;

import java.util.Date;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Event.Listener;
import com.primateer.daikoku.model.ShoppingList;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.model.vos.ShoppingItem;
import com.primateer.daikoku.ui.actions.FormAction;
import com.primateer.daikoku.ui.actions.TabDialogAction;
import com.primateer.daikoku.ui.dialogs.CatalogView;
import com.primateer.daikoku.ui.dialogs.DateSpanView;
import com.primateer.daikoku.ui.dialogs.FormFragment;
import com.primateer.daikoku.ui.views.lists.DataRowListAdapter;
import com.primateer.daikoku.ui.views.widgets.AddButton;
import com.primateer.daikoku.ui.views.widgets.DeleteRowButton;
import com.primateer.daikoku.ui.views.widgets.ListWidget;
import com.primateer.daikoku.ui.views.widgets.row.DataRowWidget;

public class ShoppingListForm extends VoForm<ShoppingList> {

	private class ShoppingItemRowWidget extends LinearLayout implements
			DataRowWidget<ShoppingItem> {

		private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();
		private DeleteRowButton deleteButton;
		private TextView amountView;
		private TextView productView;
		private CheckBox checkBox;
		private ShoppingItem item;

		public ShoppingItemRowWidget(Context context, AttributeSet attrs) {
			super(context, attrs);

			deleteButton = new DeleteRowButton(context, dispatcher);
			deleteButton.setImageResource(Application.ICON_REMOVE);
			LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.2f);
			deleteLayout.gravity = Gravity.CENTER;

			amountView = new TextView(context);
			LinearLayout.LayoutParams amountLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.4f);
			amountLayout.gravity = Gravity.CENTER;

			productView = new TextView(context);
			LinearLayout.LayoutParams productLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1.1f);
			productLayout.gravity = Gravity.CENTER;

			checkBox = new CheckBox(context);
			checkBox.setPadding(0, 0, 5, 0);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					item.setChecked(isChecked);
					if (isChecked) {
						amountView.setPaintFlags(amountView.getPaintFlags()
								| Paint.STRIKE_THRU_TEXT_FLAG);
						amountView
								.setTextColor(Application.TEXTCOLOR_LIGHTGREY);
						productView.setPaintFlags(productView.getPaintFlags()
								| Paint.STRIKE_THRU_TEXT_FLAG);
						productView
								.setTextColor(Application.TEXTCOLOR_LIGHTGREY);
					} else {
						amountView.setPaintFlags(amountView.getPaintFlags()
								& ~Paint.STRIKE_THRU_TEXT_FLAG);
						amountView.setTextColor(Application.TEXTCOLOR_BLACK);
						productView.setPaintFlags(productView.getPaintFlags()
								& ~Paint.STRIKE_THRU_TEXT_FLAG);
						productView.setTextColor(Application.TEXTCOLOR_BLACK);

					}
				}
			});
			LinearLayout.LayoutParams checkBoxLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.3f);
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
		public void addEventListener(Class<? extends Event> type,
				Listener listener) {
			dispatcher.addEventListener(type, listener);
		}

		@Override
		public void removeEventListener(Class<? extends Event> type,
				Listener listener) {
			dispatcher.removeEventListener(type, listener);
		}
	}

	private class ShoppingListAdapter extends DataRowListAdapter<ShoppingItem> {

		ShoppingList list = new ShoppingList();

		@Override
		protected DataRowWidget<ShoppingItem> newWidget(Context context) {
			return new ShoppingItemRowWidget(context);
		}

		public void setList(ShoppingList list) {
			this.list = list;
			this.setData(list.getItemList());
		}

		public ShoppingList getList() {
			return list;
		}

		public void add(final Product product) {
			FormAction<Amount> action = new FormAction<Amount>(getContext(),
					new Amount(product.getAmount()));
			action.addEventListener(FormFragment.AcceptEvent.class,
					new Event.Listener() {
						@Override
						public void onEvent(Event event) {
							@SuppressWarnings("unchecked")
							Amount amount = ((FormFragment.AcceptEvent<Amount>) event).data;

							list.add(product, amount);
							setList(list);
						}
					});
			Application.getInstance().dispatch(action);
		}
		
		public void add(Date start, Date end) {
			list.add(start,end);
			setList(list);
		}

		public void add(Recipe recipe) {
			list.add(recipe);
			setList(list);
		}
	}

	private ListWidget listView;
	private ShoppingListAdapter listAdapter;
	private AddButton addButton;

	public ShoppingListForm(Context context, AttributeSet attrs) {
		super(context, attrs);

		listView = new ListWidget(context);
		listAdapter = new ShoppingListAdapter();
		listView.setAdapter(listAdapter);

		addButton = new AddButton(context);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// MultiCatalogAction action = new MultiCatalogAction(
				// getContext(), getResources().getString(
				// R.string.title_pick_for_shopping));
				TabDialogAction action = new TabDialogAction(getContext(),
						getResources().getString(
								R.string.title_pick_for_shopping));

				Catalog<Product> productCatalog = new Catalog<Product>(
						Product.class);
				productCatalog.addEventListener(Catalog.SelectionEvent.class,
						new Event.Listener() {
							@Override
							public void onEvent(Event event) {
								@SuppressWarnings("unchecked")
								Product product = ((Catalog.SelectionEvent<Product>) event).selection;
								listAdapter.add(product);
							}
						});
				productCatalog.setTitle(getResources().getString(
						R.string.product));
				action.add(new CatalogView<Product>(getContext(),
						productCatalog));

				Catalog<Recipe> recipeCatalog = new Catalog<Recipe>(
						Recipe.class);
				recipeCatalog.addEventListener(Catalog.SelectionEvent.class,
						new Event.Listener() {
							@Override
							public void onEvent(Event event) {
								@SuppressWarnings("unchecked")
								Recipe recipe = ((Catalog.SelectionEvent<Recipe>) event).selection;
								listAdapter.add(recipe);
							}
						});
				recipeCatalog.setTitle(getResources()
						.getString(R.string.recipe));
				action.add(new CatalogView<Recipe>(getContext(), recipeCatalog));

				DateSpanView dateSpan = new DateSpanView(getContext());
				dateSpan.addEventListener(DateSpanView.DatesPickedEvent.class,
						new Listener() {
							@Override
							public void onEvent(Event event) {
								DateSpanView.DatesPickedEvent dpe = (DateSpanView.DatesPickedEvent) event;
								listAdapter.add(dpe.start,dpe.end);
							}
						});
				action.add(dateSpan);

				Application.getInstance().dispatch(action);
			}
		});

		this.addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		this.addView(addButton, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
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
		return listAdapter.getList();
	}

	@Override
	protected void fillFields(ShoppingList data)
			throws IllegalArgumentException {
		listAdapter.setList(data);
	}

}
