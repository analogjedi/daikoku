package com.primateer.daikoku.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.primateer.daikoku.R;
import com.primateer.daikoku.views.forms.Form;

public class FormFragment<T> extends DialogFragment {

	private Form<T> form;

	public void setForm(Form<T> form) {
		this.form = form;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(form.getTitle());
		form.getView().setBackgroundColor(Color.WHITE); // FIXME
		builder.setView(form.getView());
		builder.setNegativeButton(R.string.cancel,null);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		return builder.create();
	}
}
