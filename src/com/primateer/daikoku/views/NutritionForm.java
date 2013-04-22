package com.primateer.daikoku.views;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Amount;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.UnitRegistry;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.views.lists.NutrientListAdapter;
import com.primateer.daikoku.widgets.AmountWidget;
import com.primateer.daikoku.widgets.Separator;

public class NutritionForm extends LinearLayout {

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
	private Button addButton;
	private List<Nutrient.Type> nutrientTypes;

	public NutritionForm(Context context) {
		this(context, null);
	}

	public NutritionForm(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOrientation(LinearLayout.VERTICAL);

		// add button
		addButton = new Button(context);
		addButton.setText("+");
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NutritionForm.this.showAddRowDialog();
			}
		});

		// reference amount
		referenceAmount = new AmountWidget(context);
		TextView referenceLabel = new TextView(context);
		referenceLabel.setText(Application.getContext().getString(
				R.string.reference_amount));
		LayoutParams labelLayout = new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1.05f);
		labelLayout.gravity = Gravity.CENTER;
		referenceLabel.setLayoutParams(labelLayout);
		referenceLabel.setPadding(5, 0, 0, 0);
		referenceAmount.addView(referenceLabel, 0);
		referenceAmount.setUnits(UnitRegistry.getInstance().getAllUnits());
		referenceAmount.setAmount(NutrientRegistry.getInstance()
				.getDefaultReferenceAmount());
		referenceAmount.addObserver(new Observer<Amount>() {
			@Override
			public void update(Amount amount) {
				Helper.toast(amount.toString());
			}
		});

		// nutrient list
		nutrientList = new ListView(context);
		listAdapter = new NutrientListAdapter();
		listAdapter.registerDataSetObserver(new ButtonManager());
		nutrientList.setAdapter(listAdapter);
		nutrientTypes = NutrientRegistry.getInstance()
				.getDefaultNutritionFields();
		for (Nutrient.Type type : nutrientTypes) {
			listAdapter.add(type);
		}

		// composition
		this.addView(referenceAmount);
		this.addView(new Separator(context));
		this.addView(nutrientList);
		this.addView(new Separator(context));
		this.addView(addButton);
	}

	private List<Nutrient.Type> getAvailableTypes() {
		List<Nutrient.Type> availableTypes = new ArrayList<Nutrient.Type>();
		for (Nutrient.Type type : nutrientTypes) {
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

	public Nutrition getNutrition() {
		Nutrition nutrition = new Nutrition();
		nutrition.setReferenceAmount(referenceAmount.getAmount());
		nutrition.setNutrients(listAdapter.getNutrients());
		return nutrition;
	}

}
