package com.primateer.daikoku.ui.views.forms;

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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Event;
import com.primateer.daikoku.model.Nutrient;
import com.primateer.daikoku.model.NutrientRegistry;
import com.primateer.daikoku.model.NutrientSet;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.ui.views.lists.DataRowListAdapter;
import com.primateer.daikoku.ui.views.widgets.AddButton;
import com.primateer.daikoku.ui.views.widgets.AmountWidget;
import com.primateer.daikoku.ui.views.widgets.DeleteRowButton;
import com.primateer.daikoku.ui.views.widgets.ListWidget;
import com.primateer.daikoku.ui.views.widgets.ReferenceAmountWidget;
import com.primateer.daikoku.ui.views.widgets.Separator;
import com.primateer.daikoku.ui.views.widgets.row.DataRowWidget;

public class NutritionForm extends VoForm<Nutrition> {

	private static class NutrientRowWidget extends AmountWidget implements
			DataRowWidget<Nutrient> {

		private TextView label;
		private DeleteRowButton delButton;
		private Nutrient.Type type;

		public NutrientRowWidget(Context context, AttributeSet attrs) {
			super(context, attrs);

			delButton = new DeleteRowButton(context, dispatcher);
			delButton.setImageResource(Application.ICON_REMOVE);
			LinearLayout.LayoutParams deleteLayout = new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.35f);
			deleteLayout.gravity = Gravity.CENTER;

			label = new TextView(context);
			label.setLayoutParams(new LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 0.7f));

			this.addView(delButton, 0, deleteLayout);
			this.addView(label, 1);

			this.addEventListener(AmountChangedEvent.class,
					new Event.Listener() {
						@Override
						public void onEvent(Event event) {
							dispatcher.dispatch(new ChangedEvent());
						}
					});
		}

		public NutrientRowWidget(Context context) {
			this(context, null);
		}

		private void setNutrientType(Nutrient.Type type) {
			this.label.setText(type.toString());
			this.type = type;
			this.setUnits(type.defaultUnit.type);
			this.selectUnit(type.defaultUnit);
			dispatcher.dispatch(new ChangedEvent());
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
		public void setRowData(Nutrient nutrient) {
			this.setNutrientType(nutrient.type);
			super.setData(nutrient.amount);
		}

		@Override
		public Nutrient getRowData() throws InvalidDataException {
			return new Nutrient(type, getData());

		}
	}

	private static class NutrientListAdapter extends
			DataRowListAdapter<Nutrient> {

		NutrientSet nutrients;

		public NutrientListAdapter() {
			setNutrients(new NutrientSet());
		}

		@Override
		protected DataRowWidget<Nutrient> newWidget(Context context) {
			return new NutrientRowWidget(context);
		}

		public void setNutrients(NutrientSet nutrients) {
			this.nutrients = nutrients;
			super.data = nutrients;
			notifyObservers();
		}

		public Nutrient add(Nutrient.Type type) {
			Nutrient nutrient = new Nutrient(type, type.getNullAmount());
			add(nutrient);
			return nutrient;
		}

		public NutrientSet getNutrients() {
			return nutrients;
		}

		public boolean isTypeOccupied(Nutrient.Type type) {
			return nutrients.isTypeOccupied(type);
		}
	}

	private class ButtonManager extends DataSetObserver {
		@Override
		public void onChanged() {
			NutritionForm.this.addButton.setEnabled(!getAvailableTypes()
					.isEmpty());
		}
	}

	private AmountWidget referenceAmount;
	private ListWidget nutrientList;
	private NutrientListAdapter listAdapter;
	private AddButton addButton;
	private List<Nutrient.Type> permissibleTypes;

	public NutritionForm(Context context) {
		super(context);

		// add button
		addButton = new AddButton(context);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NutritionForm.this.showAddRowDialog();
			}
		});

		// reference amount
		referenceAmount = new ReferenceAmountWidget(context);

		// nutrient list
		nutrientList = new ListWidget(context);
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
		if (listAdapter.isEmpty()) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_nutrition_list_empty));
		}
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
