package com.primateer.daikoku.ui.activities;

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
import com.primateer.daikoku.model.GoalSet;
import com.primateer.daikoku.model.vos.Goal.Scope;
import com.primateer.daikoku.ui.forms.Form.InvalidDataException;
import com.primateer.daikoku.ui.forms.GoalSetForm;
import com.primateer.daikoku.ui.widgets.Separator;

public class GoalsActivity extends FragmentActivity {

	GoalSetForm form;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		form = new GoalSetForm(this);
		form.setData(new GoalSet(DBController.getInstance().loadAllGoals(
				Scope.PER_DAY)));

		LinearLayout content = new LinearLayout(this);
		content.setOrientation(LinearLayout.VERTICAL);

		ImageButton cancelButton = new ImageButton(this);
		cancelButton.setImageResource(Application.ICON_DENY);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GoalsActivity.this.finish();
			}
		});

		ImageButton okButton = new ImageButton(this);
		okButton.setImageResource(Application.ICON_ACCEPT);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					form.validate();
					DBController.getInstance().registerGoalSet(form.getData());
				} catch (InvalidDataException e1) {
					Helper.displayErrorMessage(
							GoalsActivity.this,
							getResources().getString(R.string.form_error_title),
							e1.getMessage());
					return;
				}
				GoalsActivity.this.finish();
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
						GoalsActivity.this.finish();
					}
				});
	}
}
