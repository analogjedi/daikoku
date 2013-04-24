package com.primateer.daikoku.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.Helper;
import com.primateer.daikoku.R;
import com.primateer.daikoku.model.Observable;
import com.primateer.daikoku.model.Observer;
import com.primateer.daikoku.model.SimpleObservable;
import com.primateer.daikoku.views.forms.Form;
import com.primateer.daikoku.views.forms.InvalidDataException;
import com.primateer.daikoku.views.widgets.Separator;

public class FormFragment<T> extends DialogFragment implements Observable<T> {

	private Form<T> form;
	private SimpleObservable<T> observable = new SimpleObservable<T>();

	public void setForm(Form<T> form) {
		this.form = form;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setTitle(form.getTitle());
		ViewGroup content = (ViewGroup) form.getView();
		content.setBackgroundColor(Color.WHITE); // FIXME

		ImageButton cancelButton = new ImageButton(getActivity());
		cancelButton.setImageResource(Application.ICON_DENY);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ImageButton okButton = new ImageButton(getActivity());
		okButton.setImageResource(Application.ICON_ACCEPT);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					form.validate();
				} catch (InvalidDataException e1) {
					Helper.displayErrorMessage(getActivity(), getResources()
							.getString(R.string.form_error_title), e1
							.getMessage());
					return;
				}
				try {
					observable.notifyObservers(form.getData());
				} catch (InvalidDataException e) {
					Helper.logErrorStackTrace(FormFragment.this, e,
							"Unable to notify observers");
				}
				dialog.dismiss();
			}
		});

		LinearLayout buttonLayout = new LinearLayout(getActivity());
		buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonLayout.addView(okButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));
		buttonLayout.addView(cancelButton, new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f));
		content.addView(new Separator(getActivity()));
		content.addView(buttonLayout);
		dialog.setContentView(content);

		return dialog;
	}

	@Override
	public void addObserver(Observer<T> observer) {
		observable.addObserver(observer);
	}

	@Override
	public void removeObserver(Observer<T> observer) {
		observable.removeObserver(observer);
	}
}
