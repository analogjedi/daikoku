package com.primateer.daikoku.views;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.primateer.daikoku.R;
import com.primateer.daikoku.model.vos.Product;
import com.primateer.daikoku.views.widgets.ReferenceAmountWidget;
import com.primateer.daikoku.views.widgets.UnitsAmountWidget;

public class ProductForm extends VoForm<Product> {

	private TextView heading;
	private EditText label;
	private ReferenceAmountWidget amount;
	private UnitsAmountWidget units;
	private NutritionForm nutrition;

	public ProductForm(Context context) {
		super(context);

		heading = new TextView(context);
		heading.setText(context.getString(R.string.product));

		label = new EditText(context);
		label.setHint(context.getString(R.string.label));

		amount = new ReferenceAmountWidget(context);
		amount.setLabelText(R.string.amount);

		units = new UnitsAmountWidget(context);

		TextView nutritionHeading = new TextView(context);
		nutritionHeading.setText(R.string.nutrition);
		nutrition = new NutritionForm(context);
		nutrition.addView(nutritionHeading, 0);

		this.addView(heading);
		this.addView(label);
		this.addView(amount);
		this.addView(units);
		this.addView(nutrition);
	}

	@Override
	public void validate()
			throws com.primateer.daikoku.views.InvalidDataException {
		if (label.getText().length() < 1) {
			throw new InvalidDataException(getResources().getString(
					R.string.form_error_product_label_empty));
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
	public void setData(Product data) throws IllegalArgumentException {
		// TODO Auto-generated method stub
	}

}
