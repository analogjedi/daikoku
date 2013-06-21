package com.primateer.daikoku.ui.forms;

import android.content.Context;
import android.widget.Button;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.ui.dialogs.FormFragment;
import com.primateer.daikoku.ui.widgets.LabelWidget;

public class ProductForm extends Form<Product> {

	private LabelWidget label;
	private ReferenceAmountForm amount;
	Form<Nutrition> nutritionForm;
	private UnitsAmountForm units;

	public ProductForm(final Context context) {
		super(context);

		label = new LabelWidget(context);

		amount = new ReferenceAmountForm(context);
		amount.setLabelText(R.string.amount);

		units = new UnitsAmountForm(context);

		Button nutritionButton = new Button(context);
		FormFragment<Nutrition> fragment = new FormFragment<Nutrition>();
		fragment.setupForm(context, Nutrition.class);
		fragment.connectLauncher(nutritionButton);
		nutritionForm = fragment.getForm();

		this.addView(label);
		this.addView(amount);
		this.addView(units);
		this.addView(nutritionButton);

	}

	@Override
	public void validate()
			throws Form.InvalidDataException {
		label.validate();
		if (nutritionForm.getData() == null) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_product_nutrition_is_null));
		}
		amount.validate();
		units.validate();
		nutritionForm.validate();
	}

	@Override
	protected Product gatherData() throws InvalidDataException {
		validate();
		Product data = new Product();
		data.setLabel(label.getText().toString());
		data.setAmount(amount.getData());
		data.setUnits(units.getData());
		data.setNutrition(nutritionForm.getData());
		return data;
	}

	@Override
	public void clear() {
		amount.clear();
		label.clear();
		units.clear();
		nutritionForm.clear();
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.product);
	}

	@Override
	protected void fillFields(Product data) throws IllegalArgumentException {
		label.setText(data.getLabel());
		amount.setData(data.getAmount());
		if (data.getNutrition() != null) {
			amount.setUnits(data.getNutrition().getReferenceAmount().unit.type);
		}
		units.setData(data.getUnitsAmount());
		nutritionForm.setData(data.getNutrition());
	}

}
