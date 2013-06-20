package com.primateer.daikoku.ui.views.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Component;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Event.Listener;
import com.primateer.daikoku.model.GoalRegistry;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.ui.dialogs.CatalogView;
import com.primateer.daikoku.ui.dialogs.DialogConnector;
import com.primateer.daikoku.ui.views.TabLayout;
import com.primateer.daikoku.ui.views.lists.DataRowListAdapter;
import com.primateer.daikoku.ui.views.widgets.AddButton;
import com.primateer.daikoku.ui.views.widgets.AmountWidget;
import com.primateer.daikoku.ui.views.widgets.DeleteRowButton;
import com.primateer.daikoku.ui.views.widgets.LabelWidget;
import com.primateer.daikoku.ui.views.widgets.ListWidget;
import com.primateer.daikoku.ui.views.widgets.NutritionWatchWidget;
import com.primateer.daikoku.ui.views.widgets.row.DataRowWidget;

public class RecipeForm extends Form<Recipe> {

	private LabelWidget label;
	private ListWidget componentList;
	private ComponentListAdapter listAdapter;
	private AddButton addButton;
	private ImageButton favButton;
	private boolean favButtonState;
	private NutritionWatchWidget watchWidget;

	private static class ComponentListAdapter extends
			DataRowListAdapter<Component> {

		@Override
		protected DataRowWidget<Component> newWidget(Context context) {
			return new ComponentRowWidget(context);
		}

		public void add(Product item) {
			super.add(new Component(item));
		}

	}

	private static class ComponentRowWidget extends LinearLayout implements
			DataRowWidget<Component> {

		private Component component;
		private DeleteRowButton deleteButton;
		private TextView selectView;
		private AmountWidget amountView;
		private Event.SimpleDispatcher dispatcher = new Event.SimpleDispatcher();

		public ComponentRowWidget(Context context) {
			this(context, null);
		}

		public ComponentRowWidget(Context context, AttributeSet attrs) {
			super(context, attrs);

			deleteButton = new DeleteRowButton(context, dispatcher);
			deleteButton.setImageResource(Application.ICON_REMOVE);
			LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.08f);
			deleteLayout.gravity = Gravity.CENTER;

			selectView = new TextView(context);
			selectView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			LinearLayout.LayoutParams selectLayout = new LayoutParams(0,
					LayoutParams.MATCH_PARENT, 0.42f);
			selectLayout.gravity = Gravity.CENTER_VERTICAL;
			selectView.setPadding(5, 0, 0, 5);
			selectView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Catalog<Product> catalog = new Catalog<Product>(
							Product.class);
					catalog.addEventListener(Catalog.SelectionEvent.class,
							new Event.Listener() {
								@SuppressWarnings("unchecked")
								@Override
								public void onEvent(Event event) {
									setRowData(new Component(
											((Catalog.SelectionEvent<Product>) event).selection));
								}
							});
					catalog.setTitle(getResources().getString(
							R.string.title_pick_product));
					new DialogConnector(new CatalogView<Product>(getContext(),
							catalog), getContext()).showDialog();
				}
			});

			amountView = new AmountWidget(context);
			LinearLayout.LayoutParams amountLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.6f);
			amountLayout.gravity = Gravity.CENTER;
			amountView.addEventListener(AmountWidget.AmountChangedEvent.class,
					new Event.Listener() {
						@Override
						public void onEvent(Event event) {
							Amount amount = ((AmountWidget.AmountChangedEvent) event).amount;
							component = new Component(component.product, amount);
							dispatcher.dispatch(new ChangedEvent());
						}
					});

			this.addView(deleteButton, deleteLayout);
			this.addView(selectView, selectLayout);
			this.addView(amountView, amountLayout);
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
		public void setRowData(Component data) {
			this.component = data;
			selectView.setText(data.product.toString());
			Set<Unit> permissibleUnits = new HashSet<Unit>();
			permissibleUnits.add(data.amount.unit);
			permissibleUnits.addAll(UnitRegistry.getInstance().getUnitsByType(
					data.amount.unit.type));
			if (data.product.getUnits() > 0) {
				permissibleUnits.add(Unit.UNIT_UNITS);
			}
			amountView.setUnits(new ArrayList<Unit>(permissibleUnits));
			amountView.setData(data.amount);
			dispatcher.dispatch(new ChangedEvent());
		}

		@Override
		public Component getRowData() throws InvalidDataException {
			return component;
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

	public RecipeForm(Context context) {
		super(context);

		label = new LabelWidget(context);

		componentList = new ListWidget(context);
		listAdapter = new ComponentListAdapter();
		listAdapter.addEventListener(DataRowListAdapter.ListChangedEvent.class,
				new Event.Listener() {
					@Override
					public void onEvent(Event event) {
						updateWatcher();
					}
				});
		componentList.setAdapter(listAdapter);

		addButton = new AddButton(context);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TabLayout tabs = new TabLayout(getContext());
				tabs.setTitle(getResources().getString(
						R.string.title_pick_ingredients));

				Catalog<Product> productCatalog = new Catalog<Product>(
						Product.class);
				productCatalog.addEventListener(Catalog.SelectionEvent.class,
						new Event.Listener() {
							@Override
							public void onEvent(Event event) {
								@SuppressWarnings("unchecked")
								Product item = ((Catalog.SelectionEvent<Product>) event).selection;
								listAdapter.add(item);
								if (label.isEmpty()) {
									label.setText(item.getLabel());
								}
							}
						});
				productCatalog.setTitle(getResources().getString(
						R.string.product));
				tabs.add(new CatalogView<Product>(getContext(), productCatalog));

				Catalog<Recipe> recipeCatalog = new Catalog<Recipe>(
						Recipe.class);
				recipeCatalog.addEventListener(Catalog.SelectionEvent.class,
						new Event.Listener() {
							@Override
							public void onEvent(Event event) {
								@SuppressWarnings("unchecked")
								Recipe item = ((Catalog.SelectionEvent<Recipe>) event).selection;
								Map<Product, Amount> ingredients = item
										.getIngredients();
								for (Product product : ingredients.keySet()) {
									listAdapter.add(new Component(product,
											ingredients.get(product)));
									if (label.isEmpty()) {
										label.setText(item.getLabel());
									}
								}
							}
						});
				recipeCatalog.setTitle(getResources()
						.getString(R.string.recipe));
				tabs.add(new CatalogView<Recipe>(getContext(), recipeCatalog));

				new DialogConnector(tabs, getContext()).showDialog();
			}
		});

		watchWidget = new NutritionWatchWidget(context);
		watchWidget.setGoals(GoalRegistry.getInstance().getGoals(
				Goal.Scope.PER_MEAL));

		favButton = new ImageButton(context);
		favButton.setBackgroundColor(Color.TRANSPARENT);
		favButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setFavorite(!favButtonState);
			}
		});
		setFavorite(true);
		setFavable(false);

		LinearLayout labelRow = new LinearLayout(context);
		labelRow.addView(label, new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				1.0f));
		labelRow.addView(favButton);

		this.addView(labelRow);
		this.addView(watchWidget);
		this.addView(componentList);
		this.addView(addButton);
	}

	protected void setFavable(boolean isFavable) {
		favButton.setVisibility(isFavable ? VISIBLE : GONE);
	}

	protected void setFavorite(boolean isFavorite) {
		this.favButtonState = isFavorite;
		favButton.setImageResource(isFavorite ? Application.ICON_FAVORITE
				: Application.ICON_UNFAVORITE);
	}

	private void updateWatcher() {
		try {
			watchWidget.update(RecipeForm.this.getData());
		} catch (InvalidDataException e) {
			// don't update if you can't
		}
	}

	@Override
	public void validate() throws InvalidDataException {
		label.validate();
		if (listAdapter.isEmpty()) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_recipe_list_empty));
		}
	}

	@Override
	public void clear() {
		label.clear();
		listAdapter.clear();
		setFavorite(false);
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.recipe);
	}

	@Override
	protected Recipe gatherData() throws InvalidDataException {
		Recipe result = new Recipe();
		result.setLabel(label.getText().toString());
		List<Component> components = listAdapter.getData();
		Map<Product, Amount> ingredients = new HashMap<Product, Amount>();
		for (Component comp : components) {
			ingredients.put((Product) comp.product, comp.amount);
		}
		result.setIngredients(ingredients);
		result.setFavorite(favButtonState);
		return result;
	}

	@Override
	protected void fillFields(Recipe data) throws IllegalArgumentException {
		label.setText(data.getLabel());
		List<Component> components = new ArrayList<Component>();
		Map<Product, Amount> ingredients = data.getIngredients();
		for (Product product : ingredients.keySet()) {
			components.add(new Component(product, ingredients.get(product)));
		}
		listAdapter.setData(components);
		setFavorite(data.isFavorite());
	}

}
