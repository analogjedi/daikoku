package com.primateer.daikoku.views.forms;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.views.lists.NutrientListAdapter;
import com.primateer.daikoku.views.widgets.AmountWidget;
import com.primateer.daikoku.views.widgets.ReferenceAmountWidget;
import com.primateer.daikoku.views.widgets.Separator;

public class NutritionForm extends VoForm<Nutrition> {

	private class ButtonManager extends DataSetObserver {
		@Override
		public void onChanged() {
			NutritionForm.this.addButton.setEnabled(!getAvailableTypes()
					.isEmpty());
		}
	}

	private AmountWidget referenceAmount;
	private ListView nutrientList;
	private NutrientListAdapter listAdapter;
	private ImageButton addButton;
	private List<Nutrient.Type> permissibleTypes;

	public NutritionForm(Context context) {
		super(context);

		// add button
		addButton = new ImageButton(context);
		addButton.setImageResource(Application.ICON_ADD);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NutritionForm.this.showAddRowDialog();
			}
		});

		// reference amount
		referenceAmount = new ReferenceAmountWidget(context);

		// nutrient list
		nutrientList = new ListView(context);
		nutrientList.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 1.0f));
		nutrientList.setScrollContainer(false);
		nutrientList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listAdapter = new NutrientListAdapter();
		listAdapter.registerDataSetObserver(new ButtonManager());
		nutrientList.setAdapter(listAdapter);

		permissibleTypes = NutrientRegistry.getInstance().getAllNutrientTypes();
		this.clear();

		// composition
		this.addView(referenceAmount);
		this.addView(new Separator(context));
		this.addView(nutrientList);
		this.addView(new Separator(context));
		this.addView(addButton);
	}

	private List<Nutrient.Type> getAvailableTypes() {
		List<Nutrient.Type> availableTypes = new ArrayList<Nutrient.Type>();
		for (Nutrient.Type type : permissibleTypes) {
			if (!listAdapter.isTypeOccupied(type)) {
				availableTypes.add(type);
			}
		}
		return availableTypes;
	}

	private void showAddRowDialog() {
		final List<Nutrient.Type> availableTypes = getAvailableTypes();
		if (availableTypes.isEmpty()) {
			// this should be unreachable
			return;
		}

		// don't bother the user if there is only one choice
		if (availableTypes.size() == 1) {
			listAdapter.add(availableTypes.get(0));
			return;
		}

		// show selection dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(getResources()
				.getString(R.string.choose_nutrient_type));
		ListAdapter adapter = new ArrayAdapter<Nutrient.Type>(getContext(),
				android.R.layout.simple_list_item_1, availableTypes);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listAdapter.add(availableTypes.get(which));
			}
		});
		builder.create().show();
	}

	@Override
	public void validate() throws InvalidDataException {
		gatherData();
	}

	@Override
	protected Nutrition gatherData() throws InvalidDataException {
		Nutrition nutrition = new Nutrition();
		nutrition.setReferenceAmount(referenceAmount.getData());
		nutrition.setNutrients(listAdapter.getNutrients());
		return nutrition;
	}

	@Override
	protected void fillFields(Nutrition data) throws IllegalArgumentException {
		referenceAmount.setData(data.getReferenceAmount());
		listAdapter.setNutrients(data.getNutrients());
	}

	@Override
	public void clear() {
		referenceAmount.clear();
		listAdapter.clear();
		for (Nutrient.Type type : NutrientRegistry.getInstance()
				.getDefaultNutrientTypes()) {
			listAdapter.add(type);
		}
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.nutrition);
	}
}
