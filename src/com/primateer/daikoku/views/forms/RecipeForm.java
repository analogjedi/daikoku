package com.primateer.daikoku.views.forms;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.model.vos.Recipe;
import com.primateer.daikoku.views.connector.CatalogDialogConnector;
import com.primateer.daikoku.views.lists.ComponentListAdapter;
import com.primateer.daikoku.views.widgets.LabelWidget;

public class RecipeForm extends VoForm<Recipe> {

	private EditText label;
	private ListView componentList;
	private ComponentListAdapter listAdapter;
	private ImageButton addButton;

	public RecipeForm(Context context) {
		super(context);

		label = new LabelWidget(context);

		componentList = new ListView(context);
		componentList.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 1.0f));
		componentList.setScrollContainer(false);
		componentList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listAdapter = new ComponentListAdapter();
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
					}
				});
				connector.showDialog();
			}
		});

		this.addView(label);
		this.addView(componentList);
		this.addView(addButton);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void fillFields(Recipe data) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

}
