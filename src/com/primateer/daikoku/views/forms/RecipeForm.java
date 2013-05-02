package com.primateer.daikoku.views.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Component;
import com.primateer.daikoku.model.Goal;
import com.primateer.daikoku.model.GoalRegistry;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.views.connector.CatalogDialogConnector;
import com.primateer.daikoku.views.lists.ComponentListAdapter;
import com.primateer.daikoku.views.lists.DataRowListAdapter;
import com.primateer.daikoku.views.widgets.LabelWidget;
import com.primateer.daikoku.views.widgets.NutritionWatchWidget;

public class RecipeForm extends VoForm<Recipe> {

	private LabelWidget label;
	private ListView componentList;
	private ComponentListAdapter listAdapter;
	private Button addProductButton;
	private Button addRecipeButton;
	private ImageButton favButton;
	private boolean favButtonState;
	private NutritionWatchWidget watchWidget;

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
				CatalogDialogConnector<Product> connector = new CatalogDialogConnector<Product>();
				connector.register(Product.class, getContext());
				connector.setTitle(getResources().getString(
						R.string.title_pick_product));
				connector.setSelectionObserver(new Observer<Product>() {
					@Override
					public void update(Product item) {
						listAdapter.add(item);
					}
				});
				connector.showDialog();
			}
		});

		addRecipeButton = new Button(context);
		addRecipeButton.setText(R.string.add_recipe);
		addRecipeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CatalogDialogConnector<Recipe> connector = new CatalogDialogConnector<Recipe>();
				connector.register(Recipe.class, getContext());
				connector.setTitle(getResources().getString(
						R.string.title_pick_recipe));
				connector.setSelectionObserver(new Observer<Recipe>() {
					@Override
					public void update(Recipe item) {
						Map<Product, Amount> ingredients = item
								.getIngredients();
						for (Product product : ingredients.keySet()) {
							listAdapter.add(new Component(product, ingredients
									.get(product)));
						}
					}
				});
				connector.showDialog();
			}
		});

		LinearLayout addRow = new LinearLayout(context);
		addRow.addView(addProductButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));
		addRow.addView(addRecipeButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));

		watchWidget = NutritionWatchWidget.create(context);
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
		setFavorite(false);

		LinearLayout labelRow = new LinearLayout(context);
		labelRow.addView(label, new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				1.0f));
		labelRow.addView(favButton);

		this.addView(labelRow);
		this.addView(watchWidget);
		this.addView(componentList);
		this.addView(addRow);
	}

	private void setFavorite(boolean isFavorite) {
		this.favButtonState = isFavorite;
		favButton.setImageResource(isFavorite ? Application.ICON_FAVORITE
				: Application.ICON_UNFAVORITE);
	}

	private void updateWatcher() {
		try {
			watchWidget.update(RecipeForm.this.gatherData());
		} catch (InvalidDataException e) {
			e.printStackTrace();
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
