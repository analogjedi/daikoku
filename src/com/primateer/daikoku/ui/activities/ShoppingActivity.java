package com.primateer.daikoku.ui.activities;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.ShoppingList;
import com.primateer.daikoku.ui.forms.InvalidDataException;
import com.primateer.daikoku.ui.forms.ShoppingListForm;
import com.primateer.daikoku.ui.widgets.Separator;

public class ShoppingActivity extends FragmentActivity {

	ShoppingListForm form;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		form = new ShoppingListForm(this);
		try {
			form.setData((ShoppingList) DBController.getInstance().load(
					ShoppingList.class, 0));
		} catch (SQLiteException e) {
			form.setData(new ShoppingList());
		}

		LinearLayout content = new LinearLayout(this);
		content.setOrientation(LinearLayout.VERTICAL);

		ImageButton cancelButton = new ImageButton(this);
		cancelButton.setImageResource(Application.ICON_DENY);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShoppingActivity.this.finish();
			}
		});

		ImageButton okButton = new ImageButton(this);
		okButton.setImageResource(Application.ICON_ACCEPT);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					form.validate();
					DBController.getInstance().register(form.getData());
				} catch (InvalidDataException e1) {
					Helper.displayErrorMessage(
							ShoppingActivity.this,
							getResources().getString(R.string.form_error_title),
							e1.getMessage());
					return;
				}
				ShoppingActivity.this.finish();
			}
		});

		LinearLayout buttonLayout = new LinearLayout(this);
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.addView(okButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));
		buttonLayout.addView(cancelButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));
		content.addView(form, new LayoutParams(LayoutParams.MATCH_PARENT, 0,
				1.0f));
		content.addView(new Separator(this));
		content.addView(buttonLayout);

		this.setContentView(content);
	}

	@Override
	public void onBackPressed() {
		// prevent accidental press of back button
		Helper.executeUponConfirmation(this,
				getResources().getString(R.string.confirm_leave_form_title),
				getResources().getString(R.string.confirm_leave_form_message),
				new Runnable() {
					@Override
					public void run() {
						ShoppingActivity.this.finish();
					}
				});
	}
}
