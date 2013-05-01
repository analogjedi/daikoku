package com.primateer.daikoku.views.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Component;
import com.primateer.daikoku.model.Goal;
import com.primateer.daikoku.model.GoalRegistry;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.views.connector.CatalogDialogConnector;
import com.primateer.daikoku.views.lists.ComponentListAdapter;
import com.primateer.daikoku.views.lists.DataRowListAdapter;
import com.primateer.daikoku.views.widgets.LabelWidget;
import com.primateer.daikoku.views.widgets.NutritionWatchWidget;

public class RecipeForm extends VoForm<Recipe> {

	private EditText label;
	private ListView componentList;
	private ComponentListAdapter listAdapter;
	private ImageButton addButton;
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

		addButton = new ImageButton(context);
		addButton.setImageResource(Application.ICON_ADD);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CatalogDialogConnector<Product> connector = new CatalogDialogConnector<Product>();
				connector.register(Product.class, getContext());
				connector.setSelectionObserver(new Observer<Product>() {
					@Override
					public void update(Product item) {
						listAdapter.add(item);
						// updateWatcher();
					}
				});
				connector.showDialog();
			}
		});

		watchWidget = new NutritionWatchWidget(context);
		watchWidget.setGoals(GoalRegistry.getInstance().getGoals(
				Goal.Scope.PER_MEAL));
		watchWidget.setWatchList(NutrientRegistry.getInstance().getWatchList());
		watchWidget.update(null);

		this.addView(label);
		this.addView(watchWidget);
		this.addView(componentList);
		this.addView(addButton);
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
		if (label.getText().length() < 1) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_recipe_label_empty));
		}
		if (listAdapter.isEmpty()) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_recipe_list_empty));
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

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
			// FIXME extend this to all ingredients
			ingredients.put((Product) comp.ingredient, comp.amount);
		}
		result.setIngredients(ingredients);
		return result;
	}

	@Override
	protected void fillFields(Recipe data) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

}
