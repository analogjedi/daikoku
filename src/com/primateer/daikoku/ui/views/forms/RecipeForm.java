package com.primateer.daikoku.ui.views.forms;

import java.util.ArrayList;
import java.util.Collection;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Catalog;
import com.primateer.daikoku.model.Component;
import com.primateer.daikoku.model.GoalRegistry;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.model.Unit;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.vos.Goal;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.ui.actions.CatalogAction;
import com.primateer.daikoku.ui.views.lists.DataRowListAdapter;
import com.primateer.daikoku.ui.views.widgets.LabelWidget;
import com.primateer.daikoku.ui.views.widgets.NutritionWatchWidget;
import com.primateer.daikoku.ui.views.widgets.row.DataRowWidget;

public class RecipeForm extends VoForm<Recipe> {

	private LabelWidget label;
	private ListView componentList;
	private ComponentListAdapter listAdapter;
	private Button addProductButton;
	private Button addRecipeButton;
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
		private ImageButton deleteButton;
		private TextView selectView;
		private AmountWidget amountView;
		private SimpleObservable<DataRowWidget<Component>> observable = new SimpleObservable<DataRowWidget<Component>>();

		public ComponentRowWidget(Context context) {
			this(context, null);
		}

		public ComponentRowWidget(Context context, AttributeSet attrs) {
			super(context, attrs);

			deleteButton = new ImageButton(context);
			deleteButton.setImageResource(Application.ICON_REMOVE);
			deleteButton.setBackgroundColor(Color.TRANSPARENT);
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
					catalog.addObserver(new Observer<Product>() {
						@Override
						public void update(Product item) {
							setRowData(new Component(item));
						}
					});
					CatalogAction<Product> action = new CatalogAction<Product>(
							getContext(), catalog, getResources().getString(
									R.string.title_pick_product));
					Application.getInstance().dispatch(action);
				}
			});

			amountView = new AmountWidget(context);
			LinearLayout.LayoutParams amountLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.6f);
			amountLayout.gravity = Gravity.CENTER;
			amountView.addObserver(new Observer<Amount>() {
				@Override
				public void update(Amount amount) {
					component = new Component(component.product, amount);
					observable.notifyObservers(ComponentRowWidget.this);
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
			observable.notifyObservers(this);
		}

		@Override
		public Component getRowData() throws InvalidDataException {
			return component;
		}

		@Override
		public void setOnDeleteRowListener(OnClickListener listener) {
			deleteButton.setOnClickListener(listener);
		}

		@Override
		public void addRowObserver(Observer<DataRowWidget<Component>> observer) {
			observable.addObserver(observer);
		}

		@Override
		public void removeRowObserver(
				Observer<DataRowWidget<Component>> observer) {
			observable.removeObserver(observer);
		}

	}

	public RecipeForm(Context context) {
		super(context);

		label = new LabelWidget(context);

		componentList = new ListView(context);
		componentList.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 1.0f));
		componentList.setScrollContainer(false);
		componentList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listAdapter = new ComponentListAdapter();
		listAdapter.addObserver(new Observer<DataRowListAdapter<Component>>() {
			@Override
			public void update(DataRowListAdapter<Component> observable) {
				updateWatcher();
			}
		});
		componentList.setAdapter(listAdapter);

		addProductButton = new Button(context);
		addProductButton.setText(R.string.add_product);
		addProductButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Catalog<Product> catalog = new Catalog<Product>(Product.class);
				catalog.addObserver(new Observer<Product>() {
					@Override
					public void update(Product item) {
						listAdapter.add(item);
						if (label.isEmpty()) {
							label.setText(item.getLabel());
						}
					}
				});
				CatalogAction<Product> action = new CatalogAction<Product>(
						getContext(), catalog, getResources().getString(
								R.string.title_pick_product));
				Application.getInstance().dispatch(action);
			}
		});

		addRecipeButton = new Button(context);
		addRecipeButton.setText(R.string.add_recipe);
		addRecipeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Catalog<Recipe> catalog = new Catalog<Recipe>(Recipe.class);
				catalog.setLoader(new Catalog.Loader<Recipe>() {
					@Override
					public Collection<Recipe> load(Catalog<Recipe> catalog) {
						ArrayList<Recipe> list = new ArrayList<Recipe>();
						for (Recipe recipe : DBController.getInstance()
								.loadFavoriteRecipes()) {
							list.add(recipe);
						}
						return list;
					}
				});
				catalog.addObserver(new Observer<Recipe>() {
					@Override
					public void update(Recipe item) {
						Map<Product, Amount> ingredients = item
								.getIngredients();
						for (Product product : ingredients.keySet()) {
							listAdapter.add(new Component(product, ingredients
									.get(product)));
							if (label.isEmpty()) {
								label.setText(item.getLabel());
							}
						}
					}
				});
				CatalogAction<Recipe> action = new CatalogAction<Recipe>(
						getContext(), catalog, getResources().getString(
								R.string.title_pick_recipe));
				Application.getInstance().dispatch(action);
			}
		});

		LinearLayout addRow = new LinearLayout(context);
		addRow.addView(addProductButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));
		addRow.addView(addRecipeButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));

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
		this.addView(addRow);
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
