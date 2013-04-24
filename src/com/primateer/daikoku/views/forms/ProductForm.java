package com.primateer.daikoku.views.forms;

import android.content.Context;
import android.widget.EditText;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.vos.Nutrition;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.views.widgets.LabelWidget;
import com.primateer.daikoku.views.widgets.ReferenceAmountWidget;
import com.primateer.daikoku.views.widgets.UnitsAmountWidget;

public class ProductForm extends VoForm<Product> {

	private EditText label;
	private ReferenceAmountWidget amount;
	FormDialogButton<Nutrition> nutrition;
	private UnitsAmountWidget units;

	public ProductForm(final Context context) {
		super(context);

		label = new LabelWidget(context);

		amount = new ReferenceAmountWidget(context);
		amount.setLabelText(R.string.amount);

		units = new UnitsAmountWidget(context);

		nutrition = new FormDialogButton<Nutrition>(context);
		nutrition.register(Nutrition.class);

		this.addView(label);
		this.addView(amount);
		this.addView(units);
		this.addView(nutrition);

	}

	@Override
	public void validate()
			throws com.primateer.daikoku.views.forms.InvalidDataException {
		if (label.getText().length() < 1) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_product_label_empty));
		}
		if (nutrition.getData() == null) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_product_nutrition_is_null));
		}
		amount.validate();
		units.validate();
		nutrition.validate();
	}

	@Override
	protected Product gatherData() throws InvalidDataException {
		validate();
		Product data = new Product();
		data.setLabel(label.getText().toString());
		data.setAmount(amount.getData());
		data.setUnits(units.getData());
		data.setNutrition(nutrition.getData());
		return data;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.product);
	}

	@Override
	protected void fillFields(Product data) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

}
