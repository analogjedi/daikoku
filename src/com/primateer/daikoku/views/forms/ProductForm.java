package com.primateer.daikoku.views.forms;

import android.content.Context;
import android.widget.Button;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.views.connector.FormDialogConnector;
import com.primateer.daikoku.views.widgets.LabelWidget;
import com.primateer.daikoku.views.widgets.ReferenceAmountWidget;
import com.primateer.daikoku.views.widgets.UnitsAmountWidget;

public class ProductForm extends VoForm<Product> {

	private LabelWidget label;
	private ReferenceAmountWidget amount;
	FormDialogConnector<Nutrition> nutritionConnector;
	private UnitsAmountWidget units;

	public ProductForm(final Context context) {
		super(context);

		label = new LabelWidget(context);

		amount = new ReferenceAmountWidget(context);
		amount.setLabelText(R.string.amount);

		units = new UnitsAmountWidget(context);

		Button nutritionButton = new Button(context);
		nutritionConnector = new FormDialogConnector<Nutrition>();
		nutritionConnector.register(Nutrition.class, nutritionButton);

		this.addView(label);
		this.addView(amount);
		this.addView(units);
		this.addView(nutritionButton);

	}

	@Override
	public void validate()
			throws com.primateer.daikoku.views.forms.InvalidDataException {
		label.validate();
		if (nutritionConnector.getData() == null) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_product_nutrition_is_null));
		}
		amount.validate();
		units.validate();
		nutritionConnector.validate();
	}

	@Override
	protected Product gatherData() throws InvalidDataException {
		validate();
		Product data = new Product();
		data.setLabel(label.getText().toString());
		data.setAmount(amount.getData());
		data.setUnits(units.getData());
		data.setNutrition(nutritionConnector.getData());
		return data;
	}

	@Override
	public void clear() {
		amount.clear();
		label.clear();
		units.clear();
		nutritionConnector.clear();
	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.product);
	}

	@Override
	protected void fillFields(Product data) throws IllegalArgumentException {
		label.setText(data.getLabel());
		amount.setData(data.getAmount());
		units.setData(data.getUnitsAmount());
		nutritionConnector.setData(data.getNutrition());
	}

}
